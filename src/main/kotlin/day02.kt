

private fun part1(reports: List<String>): Int {
    return reports.map { r -> r.split(" ").map(String::toInt) }
        .count { evaulateReport(it) == null }
}


private fun part2(reports: List<String>): Int {
   return reports.map { r -> r.split(" ").map(String::toInt) }
       .count { report ->
           evaulateReport(report) == null || report.indices.any { idx ->
               val modifiedReport = report.filterIndexed { index, _ -> index != idx }
               evaulateReport(modifiedReport) == null
           }
       }

//       .map {
//            val faultIdx = evaulateReport(it)
//            return@map it.filterIndexed {index,_ -> index != faultIdx}
//        }.println()
//        .filter { evaulateReport(it) == null }
//        .println()
//        .count()
}

private fun evaulateReport(report: List<Int>): Int? {
    var prevLevel = report.first()
    val rising = prevLevel > report.component2()
    report.drop(1).forEachIndexed { index, level ->
        if (Math.abs(level - prevLevel) > 3) {
            return index + 1
        }
        if (rising && level > prevLevel) {
            return index + 1
        }
        if (!rising && level < prevLevel) {
            return index  + 1
        }
        if (prevLevel == level) {
            return index + 1
        }
        prevLevel = level
    }
    return null
}

private const val day = "Day02"

fun main() {
    //todo 2 lists, sort, for each find diff and sum
    check(part1(readInput("${day}_test").println()) == 2)
    check(part2(readInput("${day}_test").println()) == 4)
//    check(part2(readInput("${day}_test2")) == 4)

    val input = readInput(day)
    check(part1(input).println() == 224) { "not 224"}
    check(part2(input).println() == 293) { "not larger than 271"}
}