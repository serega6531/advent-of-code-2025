import kotlin.math.pow

fun main() {

    fun parseRange(range: String): LongRange = range.split('-')
        .map { it.toLong() }
        .let { (start, finish) -> start..finish }

    fun parseRanges(list: String): List<LongRange> {
        return list.split(',').map { range ->
            parseRange(range)
        }
    }

    fun getInvalidIds(range: LongRange, fixedRepeats: Int? = null): Sequence<Long> {
        val first = range.first.toString()
        val last = range.last.toString()
        val repeatsRange = fixedRepeats?.let { it..it } ?: (2..last.length)
        val prefix = first.commonPrefixWith(last)

        return sequence {
            repeatsRange.forEach { repeats ->
                (1..(last.length / repeats)).forEach { repeatedLength ->
                    val prefixPart = prefix.take(repeatedLength)
                    val suffixLength = repeatedLength - prefixPart.length
                    val repeatedBase = prefixPart.padEnd(repeatedLength, '0').toLong()

                    (0..<(10.0.pow(suffixLength.toDouble()).toLong())).forEach { suffix ->
                        val repeatedPart = repeatedBase + suffix
                        val id = repeatedPart.toString().repeat(repeats).toLong()

                        if (id in range) {
                            yield(id)
                        }
                    }
                }
            }
        }
    }

    fun part1(input: String): Long {
        return parseRanges(input)
            .flatMap { getInvalidIds(it, fixedRepeats = 2) }
            .distinct()
            .sum()
    }

    fun part2(input: String): Long {
        return parseRanges(input)
            .flatMap { getInvalidIds(it) }
            .distinct()
            .sum()
    }

    val testInput = readEntireInput("Day02_test")
    val input = readEntireInput("Day02")

    check(part1(testInput) == 1227775554L)
    part1(input).println()

    check(part2(testInput) == 4174379265L)
    part2(input).println()
}
