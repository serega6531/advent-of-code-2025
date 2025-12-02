import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = readEntireInput(name).lines()

fun readEntireInput(name: String) = Path("src/$name.txt").readText().trim()

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
