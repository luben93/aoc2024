private val guard = Regex("[\\^<>v]")
private fun part1(lines: List<String>): Int {
    var initRow = lines.indexOfFirst { it.contains(guard) }
    return Regex("[\\^<>vX]")
        .findAll(simulate(lines.map { it.toMutableList() }.toMutableList(),initRow,
            guard.find(lines[initRow])?.range?.first ?: -1)
        .joinToString("")).count()
}

private tailrec fun simulate(lines: MutableList<MutableList<Char>>,currentRow: Int,currentCol: Int):
        List<String> {
    val stringed = lines.map { it.joinToString("") }
    println("row: $currentRow, col: $currentCol")
    println(stringed.joinToString("\n"))
//    if( !lines.indices.contains(currentRow) || !lines[0].indices.contains(currentCol) ) return stringed
    val currentDirection = lines[currentRow][currentCol]

    var (nextRow,nextCol) = when(currentDirection) {
        'v' -> move( 1,0,currentRow, currentCol)
        '^' -> move( -1,0,currentRow, currentCol)
        '<' -> move( 0,-1,currentRow, currentCol)
        '>' -> move( 0,1,currentRow, currentCol)
        else -> currentRow to currentCol // todo what has happened here?
    }
    if( !lines.indices.contains(nextRow) || !lines[0].indices.contains(nextCol) ) return stringed

    return if(lines[nextRow][nextCol] == '#') {
        val newDirection = when(currentDirection) {
            'v' -> '<'
            '^' -> '>'
            '<' -> '^'
            '>' -> 'v'
            else -> currentDirection
        }
        lines[currentRow][currentCol] = newDirection
        simulate(lines,currentRow,currentCol)
    }else {
        lines[nextRow][nextCol] = currentDirection
        lines[currentRow][currentCol] = 'X'
        simulate(lines,nextRow,nextCol)
    }
}

fun move(rowStep:Int,colStep:Int,currentRow: Int,
         currentCol:Int): Pair<Int, Int> {

    return currentRow+rowStep to currentCol+colStep
}


private fun part2(lines: List<String>): Int {
return 0
}

private const val fileName = "Day06"

fun main() {
    //todo 2 lists, sort, for each find diff and sum
    check(part1(readInput(fileName+"_test")).println() == 41)
    check(part2(readInput(fileName+"_test")).println() == 0)

    val input = readInput(fileName)
    part1(input).println()
    part2(input).println()
}