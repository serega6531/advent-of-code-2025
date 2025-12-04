import kotlin.math.pow

fun main() {

    fun parseBank(line: String): List<Int> {
        return line.map { it.digitToInt() }
    }

    fun solve(bank: List<Int>, remainingDigits: Int): Long {
        val (maxIndex, maxValue) = bank.dropLast(remainingDigits - 1)
            .withIndex()
            .maxBy { it.value }

        return if (remainingDigits == 1) {
            maxValue.toLong()
        } else {
            val scaledValue = maxValue * 10.0.pow(remainingDigits - 1).toLong()
            val remaining = bank.subList(maxIndex + 1, bank.size)
            scaledValue + solve(remaining, remainingDigits - 1)
        }
    }

    fun part1(input: List<String>): Long {
        return input
            .map { parseBank(it) }
            .sumOf { solve(it, 2) }
    }

    fun part2(input: List<String>): Long {
        return input
            .map { parseBank(it) }
            .sumOf { solve(it, 12) }
    }

    val testInput = readInput("Day03_test")
    val input = readInput("Day03")

    check(part1(testInput) == 357L)
    part1(input).println()

    check(part2(testInput) == 3121910778619L)
    part2(input).println()
}
