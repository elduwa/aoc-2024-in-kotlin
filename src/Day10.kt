fun main() {

    fun stepInDirection(location : Pair<Int,Int>, direction : Direction): Pair<Int, Int> {
        return location.first + direction.dir.first to location.second + direction.dir.second
    }

    fun part1(input: List<String>): Int {
        val topoMap = parseIntGrid(input)

        var result = 0

        for (j in topoMap.indices) {
            for (i in topoMap[0].indices) {
                if (topoMap[j][i] == 0) {
                    val visitedPeaks = mutableSetOf<Pair<Int,Int>>()
                    val queue = ArrayDeque(listOf(i to j))

                    while (queue.isNotEmpty()) {
                        val currentLocation = queue.removeFirst()

                        if (topoMap[currentLocation.second][currentLocation.first] == 9) {
                            visitedPeaks.add(currentLocation)
                            continue
                        }

                        val validAdjacentLocations = Direction.entries.map { stepInDirection(currentLocation, it) }
                            .filter { loc -> loc.first in topoMap[0].indices && loc.second in topoMap.indices }
                            .filter { loc -> topoMap[loc.second][loc.first] == topoMap[currentLocation.second][currentLocation.first] + 1 }

                        queue.addAll(validAdjacentLocations)
                    }
                    result += visitedPeaks.size
                }
            }
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val topoMap = parseIntGrid(input)

        var result = 0

        for (j in topoMap.indices) {
            for (i in topoMap[0].indices) {
                if (topoMap[j][i] == 0) {
                    var trailRating = 0
                    val queue = ArrayDeque(listOf(i to j))

                    while (queue.isNotEmpty()) {
                        val currentLocation = queue.removeFirst()

                        if (topoMap[currentLocation.second][currentLocation.first] == 9) {
                            trailRating++
                            continue
                        }

                        val validAdjacentLocations = Direction.entries.map { stepInDirection(currentLocation, it) }
                            .filter { loc -> loc.first in topoMap[0].indices && loc.second in topoMap.indices }
                            .filter { loc -> topoMap[loc.second][loc.first] == topoMap[currentLocation.second][currentLocation.first] + 1 }

                        queue.addAll(validAdjacentLocations)
                    }
                    result += trailRating
                }
            }
        }
        return result
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()

}
