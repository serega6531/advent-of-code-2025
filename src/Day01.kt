fun main() {
    fun applyRotation(value: Int, rotation: String): Int {
        val left = rotation[0] == 'L'
        val turns = rotation.substring(1).toInt()
        val modifier = (if (left) -1 else 1) * turns

        return (value + modifier).mod(100)
    }

    fun countZeroTurns(value: Int, rotation: String): Int {
        val left = rotation[0] == 'L'
        val turns = rotation.substring(1).toInt()

        val base = (if (left) 100 - value else value) % 100
        val new = base + turns
        val crosses = new / 100

        return crosses
    }

    fun part1(input: List<String>): Int {
        return generateSequence(50 to 0) { (position, index) ->
            Pair(
                applyRotation(position, input[index]),
                index + 1
            )
        }
            .drop(1)
            .take(input.size)
            .count { (position, _) -> position == 0 }
    }

    fun part2(input: List<String>): Int {
        return generateSequence(Triple(50, 0, 0)) { (position, index, crosses) ->
            Triple(
                applyRotation(position, input[index]),
                index + 1,
                countZeroTurns(position, input[index]) + crosses)
        }
            .drop(1)
            .take(input.size)
            .last()
            .third
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
