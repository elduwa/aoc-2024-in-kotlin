fun main() {

    val regex = """(mul\(([0-9]{1,3}),([0-9]{1,3})\))""".toRegex()


    fun part1(factors : List<Pair<Int,Int>>): Int {
        return factors.sumOf { it.first * it.second }
    }

    fun part2(reports : List<List<Int>>): Int {
        return 0
    }

    fun parseMemory(input: List<String>) : List<Pair<Int,Int>> {
        val factors = mutableListOf<Pair<Int,Int>>()
        input.forEach { line ->
            val matches = regex.findAll(line)
            val lineFactors = matches.map { it.groupValues[2].toInt() to it.groupValues[3].toInt() }.toList()
            factors += lineFactors
        }
        return factors.toList()
    }

    // Test if implementation meets criteria from the description, like:
    // check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day03_test")
    val testFactors = parseMemory(testInput)
    check(part1(testFactors) == 161)
    //check(part2(testReports) == 7)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    val factors = parseMemory(input)
    part1(factors).println()
    //part2(locIdLists).println()
}
