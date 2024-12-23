import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
//fun readInput(name: String) = Path("/$name.txt").readLines()

fun readInput(fileName: String): List<String> {
    val classLoader = Thread.currentThread().contextClassLoader
    val resource = classLoader.getResource("$fileName.txt") ?: throw IllegalArgumentException("File not found: $fileName.txt")
    ("File " +
            "not found: $fileName")
    return File(resource.toURI()).readLines() // Reads the file line by line
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun<T:Any> T.println() = this.also { println(it) }
