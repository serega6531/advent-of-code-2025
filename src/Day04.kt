import java.util.LinkedList

fun main() {

    fun parseGrid(lines: List<String>): List<List<Boolean>> {
        return lines.map { line ->
            line.map { it == '@' }
        }
    }

    fun isAccessible(grid: List<List<Boolean>>, yx: YX, removed: Set<YX> = emptySet()): Boolean {
        val maxY = grid.lastIndex
        val maxX = grid.first().lastIndex

        val fullNeighbors = neighborDirections.count { direction ->
            val neighbor = yx + direction
            neighbor.isInside(maxY, maxX) && grid[neighbor] && neighbor !in removed
        }

        return fullNeighbors < 4
    }

    fun part1(input: List<String>): Int {
        val grid = parseGrid(input)

        return grid.withIndex().sumOf { (y, row) ->
            row.withIndex().count { (x, isFull) ->
                isFull && isAccessible(grid, YX(y, x))
            }
        }
    }

    fun part2(input: List<String>): Int {
        val grid = parseGrid(input)
        val maxY = grid.lastIndex
        val maxX = grid.first().lastIndex

        val removed = mutableSetOf<YX>()
        val remaining = (0..maxY).asSequence()
            .flatMap { y ->
                (0..maxX).map { x -> YX(y, x) }
            }
            .filter { (y, x) -> grid[y][x] }
            .toCollection(LinkedList())

        while (remaining.isNotEmpty()) {
            val yx = remaining.removeFirst()
            val isFull = grid[yx]

            if (yx !in removed && isFull && isAccessible(grid, yx, removed)) {
                removed.add(yx)
                remaining.addAll(neighborDirections.map { yx + it }.filter { it.isInside(maxY, maxX) })
            }
        }

        return removed.size
    }

    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    check(part1(testInput) == 13)
    part1(input).println()

    check(part2(testInput) == 43)
    part2(input).println()
}
