fun main() {

    fun parseGrid(lines: List<String>): List<List<Boolean>> {
        return lines.map { line ->
            line.map { it == '@' }
        }
    }

    fun isAccessible(grid: List<List<Boolean>>, y: Int, x: Int): Boolean {
        val directions = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
        val maxY = grid.lastIndex
        val maxX = grid.first().lastIndex

        val fullNeighbors = directions.count { (dy, dx) ->
            val neighborY = y + dy
            val neighborX = x + dx
            neighborY in 0..maxY && neighborX in 0..maxX && grid[neighborY][neighborX]
        }

        return fullNeighbors < 4
    }

    fun part1(input: List<String>): Int {
        val grid = parseGrid(input)

        return grid.withIndex().sumOf { (y, row) ->
            row.withIndex().count { (x, isFull) ->
                isFull && isAccessible(grid, y, x)
            }
        }
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    check(part1(testInput) == 13)
    part1(input).println()

    check(part2(testInput) == 123)
    part2(input).println()
}
