import kotlin.math.abs

fun main() {

    fun isValidDiff(diff : Int, ascending : Boolean) : Boolean {
        return diff > 0 == ascending && abs(diff) <= 3 && abs(diff) > 0
    }

    fun part1(reports : List<List<Int>>): Int {
        var safeCount = 0

        for (report in reports) {
            var safe = true
            val ascending = report[1] - report[0] > 0
            for (i in 1..<report.size) {
                val diff = report[i] - report[i-1]
                if (!isValidDiff(diff, ascending)) {
                    safe = false
                    break
                }
            }
            if (safe) safeCount++
        }
        return safeCount
    }

    fun part2(listPair: Pair<List<Int>, List<Int>>): Int {
        val leftList = listPair.first
        val rightList = listPair.second

        val rightListLocIdCounts = rightList.groupingBy { it }.eachCount()

        var similarityScore = 0

        for (locId in leftList) {
            val count = rightListLocIdCounts[locId]
            count?.let { similarityScore += locId * count }
        }

        return similarityScore
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
    //check(part2(testLocIdLists) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    val locIdLists = parseReports(input)
    part1(locIdLists).println()
    //part2(locIdLists).println()
}
