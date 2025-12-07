fun main() {

    fun part1(input: List<String>): Int {
        val startX = input.first().indexOf('S')
        val maxX = input.first().lastIndex
        var splits = 0

        fun step(y: Int, beams: Set<Int>) {
            val newSplits = beams.count { input[y][it] == '^' }
            val nextBeams = beams.flatMap { x ->
                if (input[y][x] == '.') {
                    listOf(x)
                } else {
                    listOf(x - 1, x + 1).filter { it in 0..maxX }
                }
            }.toSet()

            splits += newSplits

            if (y < input.lastIndex) {
                step(y + 1, nextBeams)
            }
        }

        step(1, setOf(startX))
        return splits
    }

    fun part2(input: List<String>): Long {
        val startX = input.first().indexOf('S')
        val maxX = input.first().lastIndex

        fun step(y: Int, beams: Map<Int, Long>): Long {
            val nextBeams = mutableMapOf<Int, Long>()

            beams.forEach { (x, count) ->
                if (input[y][x] == '.') {
                    nextBeams.merge(x, count, Long::plus)
                } else {
                    listOf(x - 1, x + 1)
                        .filter { it in 0..maxX }
                        .forEach { splitX ->
                            nextBeams.merge(splitX, count, Long::plus)
                        }
                }
            }

            return if (y < input.lastIndex) {
                step(y + 1, nextBeams)
            } else {
                nextBeams.values.sum()
            }
        }

        return step(1, mapOf(startX to 1))
    }

    val testInput = readInput("Day07_test")
    val input = readInput("Day07")

    check(part1(testInput) == 21)
    part1(input).println()

    check(part2(testInput) == 40L)
    part2(input).println()
}
