import java.util.*

fun main() {

    fun parseRange(range: String): LongRange {
        return range.split('-')
            .let { (start, end) -> start.toLong()..end.toLong() }
    }

    fun parseInventory(input: String): Inventory {
        val (freshRanges, availableIngredients) = input.split("\n\n")
        return Inventory(
            freshRanges = freshRanges.lines().map { parseRange(it) },
            availableIngredients = availableIngredients.lines().map { it.toLong() }
        )
    }

    fun buildEdgesMap(ranges: List<LongRange>): NavigableMap<Long, Set<LongRange>> {
        val map = mutableMapOf<Long, MutableSet<LongRange>>()

        ranges.forEach { range ->
            map.computeIfAbsent(range.first) { mutableSetOf() }.add(range)
            map.computeIfAbsent(range.last) { mutableSetOf() }.add(range)
        }

        return TreeMap(map)
    }

    fun part1(input: String): Int {
        val (freshRanges, availableIngredients) = parseInventory(input)

        return availableIngredients.count { ingredient -> freshRanges.any { it.contains(ingredient) } }
    }

    fun part2(input: String): Long {
        val (freshRanges, _) = parseInventory(input)
        val edges = buildEdgesMap(freshRanges)

        val activeRanges = mutableSetOf<LongRange>()
        var result: Long = 0
        var currentStart: Long? = null

        edges.forEach { (index, edgeRanges) ->
            val currentlyEmpty = activeRanges.isEmpty()

            activeRanges.addAll(edgeRanges.filter { it.first == index })
            activeRanges.removeAll(edgeRanges.filter { it.last == index }.toSet())

            when {
                currentlyEmpty && activeRanges.isNotEmpty() -> {
                    currentStart = index
                }
                currentlyEmpty && activeRanges.isEmpty() -> {
                    // range starting and ending with the same value (with no other active ranges)
                    result++
                }
                activeRanges.isEmpty() -> {
                    result += index - currentStart!! + 1
                    currentStart = null
                }
            }
        }

        return result
    }

    val testInput = readEntireInput("Day05_test")
    val input = readEntireInput("Day05")

    check(part1(testInput) == 3)
    part1(input).println()

    check(part2(testInput) == 14L)
    part2(input).println()
}

private data class Inventory(val freshRanges: List<LongRange>, val availableIngredients: List<Long>)