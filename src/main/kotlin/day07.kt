private fun part1(lines: List<String>): Long {
    return lines.filter {
        val numbers = it.replace(":","").split(" ").map(String::toLong)
        val operands = numbers.drop(1)
        val target = numbers.first()
        val found = sumOrMul(operands.drop(1), target, operands.first())
        found == target
    }.sumOf { it.split(":").first().toLong() }
}

fun sumOrMul(numbers: List<Long>, target: Long,current:Long): Long {
    if(current == target) return target
    if (numbers.isEmpty()) return -1
    val added = sumOrMul(numbers.drop(1), target, current+numbers.first())
    if(added == target) return added
    return sumOrMul(numbers.drop(1), target, current*numbers.first())
}


private fun part2(lines: List<String>): Int {
    return 0
}

private const val fileName = "Day07"

fun main() {
    //todo 2 lists, sort, for each find diff and sum
    check(part1(readInput(fileName+"_test")).println() == 3749L)
    check(part2(readInput(fileName+"_test")).println() == 0)

    val input = readInput(fileName)
    part1(input).println()
    part2(input).println()
}