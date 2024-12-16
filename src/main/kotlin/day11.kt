import kotlinx.coroutines.runBlocking
import kotlin.math.log10
import kotlin.math.pow
import kotlin.time.measureTimedValue

private fun part1(lines: List<String>): Int {
    return measureTimedValue { blinkAtStones(lines.first(),25)}.println().value
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
        if (iteration + 1 == iterations) return size
    }
    return 0
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


private const val fileName = "Day11"
val cache = HashMap<Pair<Long, Int>, Long>(79*2000)

fun countStones(stone: Long, level: Int): Long =
    if (level == 0) 1L else cache.getOrPut(stone to level) {
        val digits = log10(stone.toDouble()).toInt() + 1
        when {
            stone == 0L -> countStones(1L, level - 1)
            digits % 2 == 0 -> {
                val scale = 10.0.pow(digits / 2).toLong()
                countStones(stone / scale, level - 1) +
                        countStones(stone % scale, level - 1)
            }
            else -> countStones(stone * 2024, level - 1)
        }
    }

fun streamStonesDepthFirstCountOptimized(stones: String, iterations: Int): Long {
    val input = stones.splitToSequence(" ").map { it.toLong() }
    return measureTimedValue {
        input.sumOf {countStones(it, iterations)  }
    }.also { println("cache: ${cache.size}, time: ${it}") }.value
}

fun main() {
    runBlocking {
    check(part1(readInput(fileName+"_test")).println() == 55312)
//    check(part2(readInput(fileName+"_test")).println() == 0)
    check(streamStonesDepthFirstCountOptimized(readInput(fileName + "_test").first(), 25).println() == 55312L)

        val input = readInput(fileName)
//    part1(input).println()
//    part2(input).println() // to low 897227728
        streamStonesDepthFirstCountOptimized(input.first(), 25).println()
        streamStonesDepthFirstCountOptimized(input.first(), 75).println()
    }
}