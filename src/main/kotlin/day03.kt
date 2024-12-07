

private fun part1(reports: List<String>): Int {
    val regex = "mul\\((\\d+),(\\d+)\\)".toRegex()
    return regex.findAll(reports.joinToString("")).map { match ->
            return@map match.destructured.component1().toInt() * match.destructured.component2().toInt()
        }.sum()
}


private fun part2(reports: List<String>): Int {
    var splits = reports.joinToString("").replace("do()","\ndo()").lines().map { it.split("don't" +
            "()").first() }

    return part1(splits)
}

private const val day = "Day03"

fun main() {
    //todo 2 lists, sort, for each find diff and sum
    check(part1(readInput("${day}_test")) == 161)
    check(part2(readInput("${day}_test")).println() == 48)

    val input = readInput(day)
    part1(input).println()
    part2(input).println()
}