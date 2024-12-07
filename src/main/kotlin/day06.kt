
private fun part1(lines: List<String>): Int {
return 0
}

private fun part2(lines: List<String>): Int {
return 0
}

private const val fileName = "Day05"

fun main() {
    //todo 2 lists, sort, for each find diff and sum
    check(part1(readInput(fileName+"_test")).println() == 11)
    check(part2(readInput(fileName+"_test")).println() == 31)

    val input = readInput(fileName)
    part1(input).println()
    part2(input).println()
}