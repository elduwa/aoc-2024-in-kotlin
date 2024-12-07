fun main() {

    val regex = """(mul\(([0-9]{1,3}),([0-9]{1,3})\))""".toRegex()
    val doBlockRegex = """(?<=do\(\))(.*?)(?=don't\(\))""".toRegex()

    fun parseMemory(input: List<String>) : List<Pair<Int,Int>> {
        val factors = mutableListOf<Pair<Int,Int>>()
        input.forEach { line ->
            val matches = regex.findAll(line)
            val lineFactors = matches.map { it.groupValues[2].toInt() to it.groupValues[3].toInt() }.toList()
            factors += lineFactors
        }
        return factors.toList()
    }

    fun getDoBlocks(input : List<String>) : List<String> {
        val inputText = input.joinToString("")
        val adjustedInput = mutableListOf<String>()
        adjustedInput.add(0, "do()" + inputText + "don't()")
        return adjustedInput.map { doBlockRegex.findAll(it).map { match -> match.value }.toList() }.flatten()
    }

    fun part1(input : List<String>): Int {
        val factors = parseMemory(input)
        return factors.sumOf { it.first * it.second }
    }

    fun part2(input : List<String>): Int {
        val doBlocks = getDoBlocks(input)
        val factors = parseMemory(doBlocks)
        return factors.sumOf { it.first * it.second }
    }

    // Test if implementation meets criteria from the description, like:
    // check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 48)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
