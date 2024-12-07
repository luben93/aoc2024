
private fun part1(reports: List<String>): Int {
    val target = "XMAS"
    val reversedTarget = target.reversed()

    var count = 0
    reports.forEach { row ->
        count += countOccurrences(row, target)
        count += countOccurrences(row, reversedTarget)
    }
    println("horizontal $count")
    for (col in reports[0].indices) {
        val columnString = reports.map { it[col] }.joinToString("")
        count += countOccurrences(columnString, target)
        count += countOccurrences(columnString, reversedTarget)
    }
    println("vertical $count")

    val diagonals = extractDiagonals(reports)
    diagonals.forEach { diagonal ->
        count += countOccurrences(diagonal, target)
        count += countOccurrences(diagonal, reversedTarget)
    }
    println("diagonal $count")

    return count
}

// Helper function to count occurrences of a target word in a string
private fun countOccurrences(line: String, target: String): Int {
    return line.windowed(target.length).count { it == target }
}

// Helper function to extract diagonals (both directions)
private fun extractDiagonals(grid: List<String>): List<String> {
    val rows = grid.size
    val cols = grid[0].length
    val diagonals = mutableListOf<String>()

    // Top-left to bottom-right diagonals
    for (d in 0 until rows + cols - 1) {
        val diagonal = StringBuilder()
        for (row in 0..d) {
            val col = d - row
            if (row in grid.indices && col in grid[0].indices) {
                diagonal.append(grid[row][col])
            }
        }
        diagonals.add(diagonal.toString())
    }

    // Top-right to bottom-left diagonals
    for (d in 0 until rows + cols - 1) {
        val diagonal = StringBuilder()
        for (row in 0..d) {
            val col = row - d + cols - 1
            if (row in grid.indices && col in grid[0].indices) {
                diagonal.append(grid[row][col])
            }
        }
        diagonals.add(diagonal.toString())
    }

    return diagonals
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