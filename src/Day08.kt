fun main() {

    val frequencyRegex = """[a-zA-Z0-9]""".toRegex()

    fun groupAntennaLocationsByFrequency(map: Array<CharArray>): MutableMap<Char, MutableSet<Pair<Int, Int>>> {
        val antennaLocations = mutableMapOf<Char, MutableSet<Pair<Int, Int>>>()

        for (y in map.indices) {
            map[y].forEachIndexed { x, c ->
                if (frequencyRegex.matches(c.toString())) {
                    val frequencyLocations = antennaLocations.getOrPut(c) { mutableSetOf() }
                    frequencyLocations.add(x to y)
                }
            }
        }
        return antennaLocations
    }

    fun part1(input: List<String>): Int {
        val map = parseCharacterGrid(input)
        val antennaLocations = groupAntennaLocationsByFrequency(map)

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

    fun locationOnGrid(xy : Pair<Int,Int>, rows : Int, cols : Int) : Boolean {
        return xy.first in 0..<cols && xy.second in 0..<rows
    }

    fun part2(input: List<String>): Int {
        val map = parseCharacterGrid(input)
        val antennaLocations = groupAntennaLocationsByFrequency(map)

        val antiNodeLocations = mutableSetOf<Pair<Int, Int>>()

        antennaLocations.forEach { (_, locations) ->
            val antennaPairs = locations.toList().flatMapIndexed { index, first ->
                locations.toList().subList(index + 1, locations.size).map { second -> first to second }
            }
            val antennaPairDistances = antennaPairs.map { it.first.first - it.second.first to it.first.second - it.second.second }
            val normalizedDirections = antennaPairDistances.map {
                val gcd = euclidianGCD(it.first, it.second)
                it.first / gcd to it.second / gcd
            }
            antennaPairs.zip(normalizedDirections).forEach { (pair, distance) ->
                var location = pair.first
                while (locationOnGrid(location, map.size, map[0].size)) {
                    antiNodeLocations.add(location)
                    location = location.first + distance.first to location.second + distance.second
                }
                location = pair.first.first - distance.first to pair.first.second - distance.second
                while (locationOnGrid(location, map.size, map[0].size)) {
                    antiNodeLocations.add(location)
                    location = location.first - distance.first to location.second - distance.second
                }
            }
        }
        return antiNodeLocations.size
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
