fun main() {

    fun parseConnections(input: List<String>): Map<String, List<String>> {
        return input
            .map { line -> line.split(": ") }
            .associate { (from, to) -> from to to.split(" ") }
    }

    fun countPaths(
        connections: Map<String, List<String>>,
        from: String,
        to: String,
        predicate: (PathContext) -> Boolean = { true }
    ): Long {
        val cache = mutableMapOf<PathContext, Long>()

        fun step(context: PathContext): Long {
            val next = connections[context.node] ?: return 0

            return cache.getOrPut(context) {
                next.sumOf { nextPath ->
                    val nextContext = PathContext(
                        node = nextPath,
                        dacSeen = context.dacSeen || nextPath == "dac",
                        fftSeen = context.fftSeen || nextPath == "fft"
                    )

                    if (nextPath == to) {
                        if (predicate(nextContext)) {
                            1
                        } else {
                            0
                        }
                    } else {
                        step(nextContext)
                    }
                }
            }
        }

        return step(PathContext(from, dacSeen = false, fftSeen = false))
    }

    fun part1(input: List<String>): Long {
        val connections = parseConnections(input)

        return countPaths(connections, "you", "out")
    }

    fun part2(input: List<String>): Long {
        val connections = parseConnections(input)

        return countPaths(connections, "svr", "out") { (_, dacSeen, fftSeen) -> dacSeen && fftSeen }
    }

    val testInput = readInput("Day11_test")
    val testInput2 = readInput("Day11_test2")
    val input = readInput("Day11")

    check(part1(testInput) == 5L)
    part1(input).println()

    check(part2(testInput2) == 2L)
    part2(input).println()
}

private data class PathContext(val node: String, val dacSeen: Boolean, val fftSeen: Boolean)