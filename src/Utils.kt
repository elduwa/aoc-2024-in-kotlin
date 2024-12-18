import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 *  Divide Int and apply ceil to Int
 */
fun Int.ceilDiv(other: Int): Int {
    return this.floorDiv(other) + this.rem(other).sign.absoluteValue
}

enum class Direction(val dir : Pair<Int, Int>) {
    NORTH(0 to -1),
    EAST(1 to 0),
    SOUTH(0 to 1),
    WEST(-1 to 0)
}

fun parseCharacterGrid(input : List<String>) : Array<CharArray> {
    return input.map { it.toCharArray() }.toTypedArray()
}

fun euclidianGCD(a : Int, b : Int) : Int {
    val aa = a.absoluteValue
    val bb = b.absoluteValue
    val high = if (aa >= bb) aa else bb
    val low = if (aa < bb) aa else bb

    if (low == 0) return high

    val remainder = high % low
    return euclidianGCD(low, remainder)
}
