fun main() {

    fun parseConnections(input: List<String>): Map<String, List<String>> {
        return input
            .map { line -> line.split(": ") }
            .associate { (from, to) -> from to to.split(" ") }
    }

    fun countPaths(connections: Map<String, List<String>>, from: String): Long {
        if (from == "out") {
            return 1
        }

        val next = connections[from] ?: return 0
        return next.sumOf { countPaths(connections, it) }
    }

    fun part1(input: List<String>): Long {
        val connections = parseConnections(input)

        return countPaths(connections, "you")
    }

    fun part2(input: List<String>): Long {
        TODO()
    }

    val testInput = readInput("Day11_test")
    val input = readInput("Day11")

    check(part1(testInput) == 5L)
    part1(input).println()

    check(part2(testInput) == 0L)
    part2(input).println()
}
