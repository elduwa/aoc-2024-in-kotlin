fun main() {

    val frequencyRegex = """[a-zA-Z0-9]""".toRegex()

    fun part1(input: List<String>): Int {
        val map = parseCharacterGrid(input)
        val antennaLocations = mutableMapOf<Char, MutableSet<Pair<Int, Int>>>()

        for (y in map.indices) {
            map[y].forEachIndexed { x, c ->
                if (frequencyRegex.matches(c.toString())) {
                    val frequencyLocations = antennaLocations.getOrPut(c) { mutableSetOf() }
                    frequencyLocations.add(x to y)
                }
            }
        }

        val antiNodeLocations = mutableSetOf<Pair<Int, Int>>()

        antennaLocations.forEach { (_, locations) ->
            val antennaPairs = locations.toList().flatMapIndexed { index, first ->
                locations.toList().subList(index + 1, locations.size).map { second -> first to second }
            }
            val antennaPairDistances = antennaPairs.map { it.first.first - it.second.first to it.first.second - it.second.second }
            antennaPairs.zip(antennaPairDistances).forEach { (pair, distance) ->
                antiNodeLocations.add(pair.first.first + distance.first to pair.first.second + distance.second)
                antiNodeLocations.add(pair.second.first - distance.first to pair.second.second - distance.second)
            }
        }
        val validAntiNodeLocations = antiNodeLocations.filter { loc -> loc.first in map.indices && loc.second in map[0].indices }

        return validAntiNodeLocations.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    //check(part2(testInput) == 11387L)

    val input = readInput("Day08")
    part1(input).println()
    //part2(input).println()
}
