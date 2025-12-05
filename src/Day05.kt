import java.util.TreeMap

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

    fun part1(input: String): Int {
        val (freshRanges, availableIngredients) = parseInventory(input)

        return availableIngredients.count { ingredient -> freshRanges.any { it.contains(ingredient)} }
    }

    fun part2(input: String): Int {
        TODO()
    }

    val testInput = readEntireInput("Day05_test")
    val input = readEntireInput("Day05")

    check(part1(testInput) == 3)
    part1(input).println()

    check(part2(testInput) == 14)
    part2(input).println()
}

private data class Inventory(val freshRanges: List<LongRange>, val availableIngredients: List<Long>)