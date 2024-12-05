import kotlin.math.abs

fun main() {

    fun prepareLocIdLists(input: List<String>) : Pair<List<Int>, List<Int>> {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        for (line in input) {
            val locIdPair = line.split(" ")
            leftList.add(locIdPair.first().toInt())
            rightList.add(locIdPair.last().toInt())
        }

        return Pair(leftList.sorted(), rightList.sorted())
    }

    fun part1(listPair: Pair<List<Int>, List<Int>>): Int {
        val orderedInput = listPair.first zip listPair.second

        var totalDistance = 0
        for ((l, r) in orderedInput) {
            totalDistance += abs(l - r)
        }

        return totalDistance
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

    // Test if implementation meets criteria from the description, like:
    // check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    val testLocIdLists = prepareLocIdLists(testInput)
    check(part1(testLocIdLists) == 11)
    check(part2(testLocIdLists) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    val locIdLists = prepareLocIdLists(input)
    part1(locIdLists).println()
    part2(locIdLists).println()
}
