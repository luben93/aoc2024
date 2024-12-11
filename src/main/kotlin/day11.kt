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
            renumerateStone(stone, sb)
            sb.append(' ')
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

private fun renumerateStone(stone: String, sb: StringBuilder) {
    when {
        stone == "0" -> sb.append("1")
        stone.length % 2 == 0 -> sb.append(
            "${stone.take(stone.length / 2)} ${trimStone(stone)}"
        )

        else -> sb.append(stone.toLong() * 2024)
    }
}

private fun trimStone(stone: String) =
    stone.drop(stone.length/2).trimStart('0').ifEmpty { "0" }


private fun part2(lines: List<String>): Int {
    return blinkAtStones(lines.first(),75)
}

private const val fileName = "Day11"

fun main() {
    //todo 2 lists, sort, for each find diff and sum
    check(part1(readInput(fileName+"_test")).println() == 55312)
//    check(part2(readInput(fileName+"_test")).println() == 0)

    val input = readInput(fileName)
    part1(input).println()
    part2(input).println()
}