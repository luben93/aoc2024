import kotlinx.coroutines.runBlocking
import java.time.Instant
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.coroutines.CoroutineContext
import kotlin.math.log10
import kotlin.math.pow
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

val lookup = ConcurrentHashMap(hashMapOf(0L to listOf(1L)))

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

suspend fun streamStonesAsync(stones: String, iterations: Int):Long = coroutineScope {
    var seq = stones.splitToSequence(" ").map { it.toLong() }
    repeat(iterations) { iteration ->
        seq = seq.chunked(10000)
            .flatMap { chunk ->
//                async {
                    chunk.flatMap { stone ->
                        lookup.getOrPut(stone) {
                            val digits = log10(stone.toDouble()).toInt() + 1
                            if (digits % 2 == 0) {
                                val scale = 10.0.pow(digits / 2.0).toLong()
                                listOf(stone / scale,stone % scale)
                            } else {
                                listOf(stone * 2024)
                            }
                        }
                    }
                }
//            }.map { it.await().asSequence() }.flatten()
//            }.toList().awaitAll().asSequence().flatten()//)
        println("iteration: ${iteration} count: unknown time: ${Instant.now()} cache size: " +
                "${lookup.size}")
    }
    println("processInBatches 1000000")
//    seq.asStream().parallel().count()
//    seq.chunked(10000).map {
//        async { it.count().toLong() } // Count each chunk in parallel
//    }.toList().awaitAll().sum()
    var size = 0L
    seq.processInBatches(1000000) { size += it.count().toLong() }
    return@coroutineScope size
}

fun <T,R> Sequence<T>.processInBatches(batchSize: Int, action: (List<T>) -> R) {
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        val batch = mutableListOf<T>()
        repeat(batchSize) {
            if (iterator.hasNext()) batch.add(iterator.next())
        }
        action(batch)
    }
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

fun streamStonesDepthFirstCountOptimized(stones: String, iterations: Int): Long {
    val cache = ConcurrentHashMap<Pair<Long, Int>, Long>() // Cache for (stone, remainingIterations)

    // Recursive function with caching
    fun countStones(stone: Long, remainingIterations: Int): Long {
        val cacheKey = stone to remainingIterations

        // Return cached value if already computed
        cache[cacheKey]?.let { return it }

        if (remainingIterations == 0) return 1L

        val digits = log10(stone.toDouble()).toInt() + 1
        val descendants = when {
            stone == 0L -> listOf(1L)
            digits % 2 == 0 -> {
                val scale = 10.0.pow(digits / 2.0).toLong()
                listOf(stone / scale, stone % scale)
            }
            else -> {
                listOf(stone * 2024)
            }
        }

        // Sum up counts recursively
        val totalCount = descendants.sumOf { countStones(it, remainingIterations - 1) }

        return cache.getOrPut(cacheKey) {totalCount}
    }

    return stones.split(" ").map { it.toLong() }.sumOf { countStones(it, iterations) }
}

fun main() {
    runBlocking {
        //todo 2 lists, sort, for each find diff and sum
//    check(part1(readInput(fileName+"_test")).println() == 55312)
//    check(part2(readInput(fileName+"_test")).println() == 0)
        check(streamStonesDepthFirstCountOptimized(readInput(fileName + "_test").first(), 25).println() == 55312L)

        val input = readInput(fileName)
//    part1(input).println()
//    part2(input).println() // to low 897227728
        streamStonesDepthFirstCountOptimized(input.first(), 75).println()
    }
}