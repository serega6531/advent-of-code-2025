import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = readEntireInput(name).lines()

fun readEntireInput(name: String) = Path("src/$name.txt").readText()

inline fun <T> Iterable<Iterable<T>>.forEachIndexed(action: (y: Int, x: Int, T) -> Unit) {
    this.forEachIndexed { y, line ->
        line.forEachIndexed { x, item ->
            action(y, x, item)
        }
    }
}

@JvmName("forEachIndexedString")
inline fun Iterable<String>.forEachIndexed(action: (y: Int, x: Int, Char) -> Unit) {
    this.forEachIndexed { y, line ->
        line.forEachIndexed { x, item ->
            action(y, x, item)
        }
    }
}

operator fun <T> List<List<T>>.get(yx: YX): T {
    return this[yx.y][yx.x]
}

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Creates a memoized version of a function that caches results.
 */
fun <A, R> memoize(fn: (A) -> R): (A) -> R {
    val cache = mutableMapOf<A, R>()
    return { arg ->
        cache.getOrPut(arg) { fn(arg) }
    }
}

data class YX(val y: Int, val x: Int) {

    fun distanceTo(other: YX): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    override fun toString(): String = "($y, $x)"

    operator fun plus(dir: DirectionOffset): YX {
        return YX(this.y + dir.dy, this.x + dir.dx)
    }

    fun isInside(maxY: Int, maxX: Int): Boolean {
        return y in 0..maxY && x in 0..maxX
    }
}

data class DirectionOffset(val dy: Int, val dx: Int)

val neighborDirections = listOf(
    DirectionOffset(-1, -1),
    DirectionOffset(-1, 0),
    DirectionOffset(-1, 1),
    DirectionOffset(0, -1),
    DirectionOffset(0, 1),
    DirectionOffset(1, -1),
    DirectionOffset(1, 0),
    DirectionOffset(1, 1)
)

val cardinals = listOf(
    DirectionOffset(-1, 0),
    DirectionOffset(0, 1),
    DirectionOffset(1, 0),
    DirectionOffset(0, -1)
)