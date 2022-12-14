import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

data class Position(
    val x: Int,
    val y: Int,
) {
    fun neighbours() = listOf(
        Position(x, y + 1),
        Position(x, y - 1),
        Position(x + 1, y),
        Position(x - 1, y),
    )

    override fun toString(): String {
        return "[$x,$y]"
    }
}