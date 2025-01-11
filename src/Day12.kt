fun main() {

    data class Plot(val plantType : Char, var visited : Boolean)

    fun part1(input: List<String>): Int {
        val garden = parseCharacterGrid(input).map { row ->
            row.map { char -> Plot(char, false) }.toTypedArray()
        }.toTypedArray()

        val plantToAreaPerimeter = mutableListOf<Pair<Char, Pair<Int,Int>>>()

        for (row in garden.indices) {
            while (garden[row].any { !it.visited }) {
                val col = garden[row].indexOfFirst { !it.visited }
                val startPlot = garden[row][col]
                val startLocation = row to col
                var area = 0
                var perimeter = 0

                val frontier = ArrayDeque(listOf(startLocation))

                while (frontier.isNotEmpty()) {
                    val currentLocation = frontier.removeFirst()
                    val currentPlot = garden[currentLocation.first][currentLocation.second]
                    currentPlot.visited = true
                    area++
                    for (direction in Direction.entries) {
                        val adjacentLocation = currentLocation.first + direction.dir.second to currentLocation.second + direction.dir.first
                        if (adjacentLocation.first < 0 || adjacentLocation.first >= garden.size
                            || adjacentLocation.second < 0 || adjacentLocation.second >= garden[0].size) {
                            perimeter++
                        }
                        else {
                            val adjacentPlot = garden[adjacentLocation.first][adjacentLocation.second]
                            if (adjacentPlot.plantType != currentPlot.plantType) {
                                perimeter++
                            }
                            else if (!adjacentPlot.visited && !frontier.contains(adjacentLocation)) {
                                frontier.add(adjacentLocation)
                            }
                        }
                    }
                }
                plantToAreaPerimeter.add(startPlot.plantType to (area to perimeter))
            }

        }
        return plantToAreaPerimeter.sumOf { it.second.first * it.second.second }
    }

    fun part2(input: List<String>): Int {
        // TODO: DFS instead of BFS
        return 0
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 1930)
    //check(part2(testInput) == 81)

    val input = readInput("Day12")
    part1(input).println()
    //part2(input).println()

}
