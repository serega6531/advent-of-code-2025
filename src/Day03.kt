fun main() {

    fun parseBank(line: String): List<Int> {
        return line.map { it.digitToInt() }
    }

    fun part1(input: List<String>): Int {
        fun solve(line: String): Int {
            val bank = parseBank(line)

            val (firstIndex, firstValue) = bank.dropLast(1)
                .withIndex()
                .maxBy { it.value }

            val secondValue = bank.drop(firstIndex + 1).max()

            return firstValue * 10 + secondValue
        }

        return input.sumOf(::solve)
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    val testInput = readInput("Day03_test")
    val input = readInput("Day03")

    check(part1(testInput) == 357)
    part1(input).println()

    check(part2(testInput) == 123)
    part2(input).println()
}
