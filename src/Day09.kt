import java.util.*
import kotlin.math.abs
import kotlin.math.pow

fun main() {

    fun parseInput(input: List<String>): List<YX> {
        return input
            .map { line -> line.split(',')
            .let { (x, y) -> YX(y.toInt(), x.toInt()) } }
    }

    fun getPairs(tiles: List<YX>) = sequence {
        tiles.forEachIndexed { index, t1 ->
            tiles.subList(index + 1, tiles.size).forEach { t2 ->
                yield(Pair(t1, t2))
            }
        }
    }

    fun getArea(t1: YX, t2: YX): Long {
        return (abs(t1.x - t2.x) + 1).toLong() * (abs(t1.y - t2.y) + 1).toLong()
    }

    fun part1(input: List<String>): Long {
        val tiles = parseInput(input)

        return getPairs(tiles).maxOf { (t1, t2) -> getArea(t1, t2) }
    }

    fun part2(input: List<String>): Long {
       TODO()
    }

    val testInput = readInput("Day09_test")
    val input = readInput("Day09")

    check(part1(testInput) == 50L)
    part1(input).println()

    check(part2(testInput) == 24L)
    part2(input).println()
}
