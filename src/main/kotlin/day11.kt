import kotlinx.coroutines.runBlocking
import java.time.Instant
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.streams.asStream

private fun part1(lines: List<String>): Int {
    var stones = lines.first().split(" ")
    return blinkAtStones(lines.first(),25)
}

fun blinkAtStones(stoneArragnmnet: String,iterations: Int):Int{
    var stones = stoneArragnmnet
    val sb = StringBuilder()
    for (iteration in 0..iterations) {
        stones.split(" ")
            .filter { it.isNotEmpty() }
            .forEach { stone ->
            sb.append(renumerateStone(stone)).append(' ')
        }
        stones = sb.toString()
        sb.clear()
        val size = stones.count { it == ' ' }
        println("iteration: ${iteration+1} count: ${size} newStones: ${stones.take
            (200)}")
        //$stones
        if (iteration + 1 == iterations) return size
    }
    return 0
}

val lookup = hashMapOf(0L to listOf(1L))

tailrec fun streamStones(stones: String, iterations: Int, seq: Sequence<Long> = stones
    .splitToSequence(" ").map { it.toLong() }): Long {
    val list = seq.map { i ->
        lookup.computeIfAbsent(i) {
                if(hasEvenNumberOfDigits(it)) {
                    val stone = it.toString()
                    listOf(stone.take(stone.length / 2).toLong(), trimStone(stone))
                } else listOf(it * 2024)
            }}
    println("iteration: $iterations count: unknown time: ${Instant.now()}")
    if (iterations <= 1) return list.fold(0L) { acc, l -> l.count() + acc }
    return streamStones(stones, iterations - 1, list.flatMap { it })
}

suspend fun streamStonesAsync(stones: String, iterations: Int): Long {
    val seq = arrayListOf( stones.splitToSequence(" ").map { it.toLong() })
    while (seq.size < iterations+1) {
        seq.add(seq.last().chunked(1000) // Split into chunks of 1000 elements
            .map { chunk ->
                coroutineScope {
                    async {
                    chunk.flatMap { stone ->
                        lookup.computeIfAbsent(stone) {
                            if (hasEvenNumberOfDigits(stone)) {
                                val stoneStr = stone.toString()
                                listOf(stoneStr.take(stoneStr.length / 2).toLong(), trimStone(stoneStr))
                            } else {
                                listOf(stone * 2024)
                            }
                        }
                    }
                }
                }//.awaitAll()
            }.toList().awaitAll() // Wait for all chunks to complete
            .flatten().asSequence())
        println("iteration: ${seq.size} count: ${seq.last().asStream().count()} time: ${Instant
            .now()}")
    }

    return seq.last().asStream().count()

}

fun hasEvenNumberOfDigits(number: Long): Boolean {
//    if (number == 0L) return false // 0 has 1 digit, which is odd
    val digits = (Math.log10(number.toDouble()) + 1).toInt()
    return digits % 2 == 0
}

private fun renumerateStone(stone: String): String {
    return when {
        stone == "0" -> "1"
        stone.length % 2 == 0 -> "${stone.take(stone.length / 2)} ${trimStone(stone)}"
        else -> "${stone.toLong() * 2024}"
    }
}

private fun trimStone(stone: String) =
    stone.drop(stone.length/2).trimStart('0').ifEmpty { "0" }.toLong()


private fun part2(lines: List<String>): Number {
    return streamStones(lines.first(),75)
}

private const val fileName = "Day11"

fun main() {
    runBlocking {
        //todo 2 lists, sort, for each find diff and sum
//    check(part1(readInput(fileName+"_test")).println() == 55312)
//    check(part2(readInput(fileName+"_test")).println() == 0)
        check(streamStonesAsync(readInput(fileName + "_test").first(), 25) == 55312L)

        val input = readInput(fileName)
//    part1(input).println()
//    part2(input).println()
        streamStonesAsync(input.first(), 75).println()
    }
}