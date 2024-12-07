
// XMAS
private fun part1(reports: List<String>): Int {
    val regex = Regex("XMAS|SAMX")
//    val horizontal =regex.findAll(reports.joinToString(" ")).count()
    val sb = StringBuilder()
    fun checkDiagonal(word:String, startRow: Int, startCol: Int, colStep: Int): Boolean {
        for (i in word.indices) {
            val newRow = startRow + i
            val newCol = startCol + i * colStep
            if (newRow !in reports.indices || newCol !in reports.indices ||
                reports[newRow][newCol] != word[i]) {
                return false
            }
        }
        return true
    }
    var diagonal = 0
    var horizontal = 0
    for (row in reports.indices){
        for (col in reports[row].indices){
            sb.append(reports[col][row])
            if (checkDiagonal("XMAS",row, col, 1)) diagonal++
            if (checkDiagonal("XMAS",row, col, -1)) diagonal++
            if (checkDiagonal("SAMX",row, col, 1)) diagonal++
            if (checkDiagonal("SAMX",row, col, -1)) diagonal++
        }
        sb.append(" ")
        horizontal += regex.findAll(reports[row]).count()
    }
    val vertiacal =regex.findAll(sb.toString()).count()

    println("horizontal:$horizontal vertical: $vertiacal diagonal: $diagonal")

    return horizontal+vertiacal+diagonal
}


private fun part2(reports: List<String>): Int {
    return 0
}

private const val day = "Day04"

fun main() {
    //todo 2 lists, sort, for each find diff and sum
    check(part1(readInput("${day}_test")).println() == 18)
    check(part2(readInput("${day}_test")).println() == 0)

    val input = readInput(day)
    part1(input).println()
    part2(input).println()
}