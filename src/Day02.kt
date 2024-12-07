import kotlin.math.abs

fun main() {

    fun isValidDiff(diff : Int, ascending : Boolean) : Boolean {
        return diff > 0 == ascending && abs(diff) <= 3 && abs(diff) > 0
    }

    fun getDiffs(report: List<Int>) = (1..<report.size)
        .map { report[it] - report[it - 1] }

    fun part1(reports : List<List<Int>>): Int {
        var safeCount = 0

        reports.forEach { report ->
            val diffs = getDiffs(report)
            val ascending = diffs.sum() > 0
            val safe = diffs.all { isValidDiff(it, ascending) }
            if (safe) safeCount++
        }
        return safeCount
    }

    fun part2(reports : List<List<Int>>): Int {
        var safeCount = 0

        reports.forEach { report ->
            val diffs = getDiffs(report)
            val ascCount = diffs.count { it > 0 }
            val descCount = diffs.count { it < 0 }
            val stagCount = diffs.count { it == 0 }
            val ascending = ascCount > descCount
            if (ascending && descCount + stagCount <= 1 || !ascending && ascCount + stagCount <= 1) {
                var safe = diffs.all { isValidDiff(it, ascending) }
                if (!safe) {
                    val falseDiff = diffs.indexOfFirst { diff -> !isValidDiff(diff, ascending) }
                    val reportCopy = report.toMutableList()

                    if (falseDiff == 0) {
                        if (isValidDiff(diffs[1], ascending)) {
                            reportCopy.removeAt(0)
                        } else if (isValidDiff(report[2] - report[0], ascending)) {
                            reportCopy.removeAt(1)
                        }
                    } else if (falseDiff == diffs.size - 1) {
                        if (isValidDiff(diffs[diffs.size - 2], ascending)) {
                            reportCopy.removeAt(diffs.size)
                        } else if (isValidDiff(report[diffs.size] - report[diffs.size - 2], ascending)) {
                            reportCopy.removeAt(diffs.size - 1)
                        }
                    } else {
                        reportCopy.removeAt(falseDiff + 1)
                    }
                    val adjustedDiffs = getDiffs(reportCopy)
                    safe = adjustedDiffs.all { isValidDiff(it, ascending) }
                    if (!safe) println(reportCopy)
                }
                if (safe) safeCount++ else println(report)
            }
        }
        return safeCount
    }

    fun parseReports(input: List<String>) : List<List<Int>> {
        val reports = mutableListOf<List<Int>>()
        for (line in input) {
            val report = line.split(" ").map { it.toInt() }
            reports.add(report)
        }
        return reports
    }

    // Test if implementation meets criteria from the description, like:
    // check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day02_test")
    val testReports = parseReports(testInput)
    check(part1(testReports) == 2)
    check(part2(testReports) == 7)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    val locIdLists = parseReports(input)
    part1(locIdLists).println()
    part2(locIdLists).println()
}
