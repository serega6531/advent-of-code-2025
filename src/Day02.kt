fun main() {

    fun parseRange(range: String): LongRange = range.split('-')
        .map { it.toLong() }
        .let { (start, finish) -> start..finish }

    fun parseRanges(list: String): List<LongRange> {
        return list.split(',').map { range ->
            parseRange(range)
        }
    }

    fun part1(input: String): Long {
        fun isInvalidId(id: Long): Boolean {
            val str = id.toString()
            val length = str.length

            if (length % 2 == 1) return false
            return str.take(length / 2) == str.drop(length / 2)
        }

        val ranges = parseRanges(input)
        return ranges.asSequence()
            .flatten()
            .filter { isInvalidId(it) }
            .sum()
    }

    fun part2(input: String): Long {
        val getDivisors = memoize { n: Int ->
            (1..(n / 2))
                .filter { n % it == 0 }
                .toList()
        }

        fun chunksMatch(str: String, len: Int): Boolean {
            val totalChunks = str.length / len
            return (1..<totalChunks).all { i ->
                str.regionMatches(0, str, i * len, len)
            }
        }

        fun isInvalidId(id: Long): Boolean {
            val str = id.toString()
            val length = str.length

            return getDivisors(length).any {
                chunksMatch(str, it)
            }
        }

        val ranges = parseRanges(input)
        return ranges.asSequence()
            .flatten()
            .filter { isInvalidId(it) }
            .sum()
    }

    val testInput = readEntireInput("Day02_test")
    val input = readEntireInput("Day02")

    check(part1(testInput) == 1227775554L)
    part1(input).println()

    check(part2(testInput) == 4174379265L)
    part2(input).println()
}
