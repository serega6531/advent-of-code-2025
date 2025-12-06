fun main() {

    fun parseProblemsVertically(input: String): List<Problem> {
        val lines = input.lines()
        val cleaned = lines.map { line -> line.split(' ').filter { it.isNotBlank() } }

        return cleaned.first().indices.map { i ->
            val problemParts = cleaned.map { it[i] }
            val operator = problemParts.last()[0]
            val operands = problemParts.dropLast(1).map { it.toLong() }

            Problem(operator, operands)
        }
    }

    fun solve(problem: Problem): Long {
        return when (problem.operator) {
            '+' -> problem.operands.sum()
            '*' -> problem.operands.reduce { acc, l -> acc * l }
            else -> throw IllegalArgumentException("Unknown operator ${problem.operator}")
        }
    }

    fun part1(input: String): Long {
        val problems = parseProblemsVertically(input)
        return problems.sumOf { solve(it) }
    }

    fun part2(input: String): Long {
        TODO()
    }

    val testInput = readEntireInput("Day06_test")
    val input = readEntireInput("Day06")

    check(part1(testInput) == 4277556L)
    part1(input).println()

    check(part2(testInput) == 14L)
    part2(input).println()
}

private data class Problem(val operator: Char, val operands: List<Long>)