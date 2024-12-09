fun main() {

    fun parsePuzzleGrid(input: List<String>) : Array<CharArray> {
        return input.map { it.toCharArray() }.toTypedArray()
    }

    fun gatherAllDirections(grid : Array<CharArray>, posX : Int, posY : Int, nRows : Int, nCols : Int) : List<String> {
        val directions = listOf(1 to 0, 1 to -1, 0 to -1, -1 to -1, -1 to 0, -1 to 1, 0 to 1, 1 to 1)
        val directionStrings = mutableListOf<String>()

        for ((x, y) in directions) {
            if (posX + 3 * x >= nCols || posX + 3 * x < 0
                || posY + 3 * y >= nRows || posY + 3 * y < 0) {
                continue
            }
            val directionCharacters = mutableListOf(grid[posY][posX])
            for (i in 1..3) {
                directionCharacters.add(grid[posY + i * y][posX + i * x])
            }
            directionStrings.add(directionCharacters.joinToString(""))
        }
        return directionStrings
    }

    fun part1(input : List<String>): Int {
        val grid = parsePuzzleGrid(input)
        val nRows = grid.size
        val nCols = grid[0].size

        var xmasCount = 0

        for (i in 0..<nRows) {
            for (j in 0..<nCols) {
                if (grid[i][j] == 'X') {
                    val directionStrings = gatherAllDirections(grid, j, i, nRows, nCols)
                    val locationCount = directionStrings.count { it == "XMAS" }
                    //println("x=$j y=$i count=$locationCount")
                    xmasCount += locationCount
                }
            }
        }

        return xmasCount
    }

    fun part2(input : List<String>): Int {
        return 0
    }

    // Test if implementation meets criteria from the description, like:
    // check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    //check(part2(testInput) == 48)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    //part2(input).println()
}
