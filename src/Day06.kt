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

    fun parseSingleProblemHorizontally(lines: List<String>): Problem {
        val operator = lines.last()[0]
        val operandLines = lines.dropLast(1)

        val maxY = operandLines.maxOf { it.length }

        val operands = (0 until maxY).reversed().map { y ->
            var operand = 0L

            operandLines
                .filter { line -> y < line.length && line[y] != ' ' }
                .forEach { line ->
                    operand = operand * 10 + line[y].digitToInt()
                }

            operand
        }

        return Problem(operator, operands)
    }

    fun parseProblemsHorizontally(input: String): List<Problem> {
        val lines = input.lines()

        var start: Int? = null
        val result = mutableListOf<Problem>()

        lines.last()
            .withIndex()
            .forEach { (x, c) ->
                if (c != ' ') { // operator on last line means new problem starts
                    if (start != null) {
                        val bounded = lines.map { it.substring(start!!, x - 1) }
                        result.add(parseSingleProblemHorizontally(bounded))
                    }

                    start = x
                }
            }

        val lastBounded = lines.map { it.substring(start!!) }
        result.add(parseSingleProblemHorizontally(lastBounded))

        return result
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
        val problems = parseProblemsHorizontally(input)
        return problems.sumOf { solve(it) }
    }

    val testInput = readEntireInput("Day06_test")
    val input = readEntireInput("Day06")

    check(part1(testInput) == 4277556L)
    part1(input).println()

    check(part2(testInput) == 3263827L)
    part2(input).println()
}

private data class Problem(val operator: Char, val operands: List<Long>)