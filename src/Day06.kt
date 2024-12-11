
fun main() {

    data class Guard(var position : Pair<Int, Int>, var direction : Direction)

    fun turnRight(guard: Guard) {
        guard.direction = when (guard.direction) {
            Direction.NORTH -> Direction.EAST
            Direction.EAST -> Direction.SOUTH
            Direction.SOUTH -> Direction.WEST
            Direction.WEST -> Direction.NORTH
        }
    }

    fun part1(input : List<String>): Int {
        val grid = parseCharacterGrid(input)
        //val gridCopy = grid.map { it.clone() }.toTypedArray()
        val cols = grid[0].size
        val rows = grid.size

        var initialPosition = 0 to 0
        for (posY in 0..<rows) {
            val posX = grid[posY].indexOfFirst { it != '.' && it != '#' }
            if (posX != -1) {
                initialPosition = posX to posY
                break
            }
        }

        val initialDirection = when (grid[initialPosition.second][initialPosition.first]) {
            '^' -> Direction.NORTH
            '>' -> Direction.EAST
            'v' -> Direction.SOUTH
            '<' -> Direction.WEST
            else -> error("Not a valid direction")
        }

        val guard = Guard(initialPosition, initialDirection)
        val visited = mutableSetOf(initialPosition)

        while (true) {
            val (posX, posY) = guard.position
            val (deltaX, deltaY) = guard.direction.dir
            var nextPosition = posX + deltaX to posY + deltaY

            if (nextPosition.first >= cols || nextPosition.first < 0
                ||nextPosition.second >= rows || nextPosition.second < 0) break

            if (grid[nextPosition.second][nextPosition.first] == '#') {
                turnRight(guard)
                nextPosition = posX + guard.direction.dir.first to posY + guard.direction.dir.second
            }

            if (nextPosition.first >= cols || nextPosition.first < 0
                ||nextPosition.second >= rows || nextPosition.second < 0) break

            guard.position = nextPosition
            //gridCopy[nextPosition.second][nextPosition.first] = 'X'
            visited.add(nextPosition)
        }


        return visited.size
    }

    fun part2(input : List<String>): Int {

        return 0
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    //check(part2(testInput) == 123)

    val input = readInput("Day06")
    part1(input).println()
    //part2(input).println()
}
