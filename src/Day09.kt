import java.util.*
import kotlin.math.abs

fun main() {

    fun parseInput(input: List<String>): List<YX> {
        return input
            .map { line ->
                line.split(',')
                    .let { (x, y) -> YX(y.toInt(), x.toInt()) }
            }
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


    fun buildRange(a: Int, b: Int): IntRange {
        val min = minOf(a, b)
        val max = maxOf(a, b)
        return min..max
    }

    fun rectangleFitsInsidePolygon(
        corners: Pair<YX, YX>,
        verticals: NavigableMap<Int, MutableList<IntRange>>,
        horizontals: NavigableMap<Int, MutableList<IntRange>>
    ): Boolean {
        val fromX = minOf(corners.first.x, corners.second.x)
        val toX = maxOf(corners.first.x, corners.second.x)
        val fromY = minOf(corners.first.y, corners.second.y)
        val toY = maxOf(corners.first.y, corners.second.y)

        val containsHorizontalWall by lazy {
            horizontals.subMap(fromY, false, toY, false).any { (_, ranges) ->
                ranges.any { it.intersects((fromX + 1)..<toX) }
            }
        }

        val containsVerticalWall by lazy {
            verticals.subMap(fromX, false, toX, false).any { (_, ranges) ->
                ranges.any { it.intersects((fromY + 1)..<toY) }
            }
        }

        val insidePolygon by lazy {
            val insideX = if (fromX + 1 <= toX - 1) fromX + 1 else fromX
            val insideY = if (fromY + 1 <= toY - 1) fromY + 1 else fromY

            val intersections = verticals.tailMap(insideX + 1, true).values.sumOf { ranges ->
                ranges.count { range -> insideY >= range.first && insideY < range.last }
            }
            intersections % 2 == 1
        }

        return !containsHorizontalWall && !containsVerticalWall && insidePolygon
    }

    fun part1(input: List<String>): Long {
        val tiles = parseInput(input)

        return getPairs(tiles).maxOf { (t1, t2) -> getArea(t1, t2) }
    }

    fun part2(input: List<String>): Long {
        val tiles = parseInput(input)

        val verticalWalls = TreeMap<Int, MutableList<IntRange>>()
        val horizontalWalls = TreeMap<Int, MutableList<IntRange>>()

        (tiles + tiles.first()).zipWithNext { (ay, ax), (by, bx) ->
            when {
                ay == by -> horizontalWalls.getOrPut(ay) { mutableListOf() } += buildRange(ax, bx)
                ax == bx -> verticalWalls.getOrPut(ax) { mutableListOf() } += buildRange(ay, by)
                else -> throw IllegalStateException("Should connect to the previous tile")
            }
        }

        val bestPair = getPairs(tiles)
            .filter { pair -> rectangleFitsInsidePolygon(pair, verticalWalls, horizontalWalls) }
            .maxBy { (t1, t2) -> getArea(t1, t2) }

        println("Best pair: $bestPair")

        return getArea(bestPair.first, bestPair.second)
    }

    val testInput = readInput("Day09_test")
    val input = readInput("Day09")

    check(part1(testInput) == 50L)
    part1(input).println()

    check(part2(testInput) == 24L)
    part2(input).println()
}
