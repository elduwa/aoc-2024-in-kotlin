
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

    fun simulateGuardPath(grid: Array<CharArray>): Pair<MutableSet<Guard>, Boolean> {
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
        var isCycle = false
        val visited = mutableSetOf(guard.copy())

        while (true) {
            val (posX, posY) = guard.position
            val (deltaX, deltaY) = guard.direction.dir
            var nextPosition = posX + deltaX to posY + deltaY

            if (nextPosition.first >= cols || nextPosition.first < 0
                || nextPosition.second >= rows || nextPosition.second < 0
            ) break

            while (grid[nextPosition.second][nextPosition.first] == '#') {
                turnRight(guard)
                nextPosition = posX + guard.direction.dir.first to posY + guard.direction.dir.second
            }

            if (nextPosition.first >= cols || nextPosition.first < 0
                || nextPosition.second >= rows || nextPosition.second < 0
            ) break

            guard.position = nextPosition
            val guardState = guard.copy()

            //gridCopy[nextPosition.second][nextPosition.first] = 'X'
            isCycle = !visited.add(guardState)
            if (isCycle) break
        }
        return visited to isCycle
    }

    fun part1(input : List<String>): Int {
        val grid = parseCharacterGrid(input)
        //val gridCopy = grid.map { it.clone() }.toTypedArray()
        val guardStates = simulateGuardPath(grid)

        return guardStates.first.map { it.position }.toSet().size
    }

    fun part2(input : List<String>): Int {
        val initialGrid = parseCharacterGrid(input)
        val initialGuardPath = simulateGuardPath(initialGrid).first

        var cycleCount = 0
        initialGuardPath.map { it.position }.toSet().forEach { position ->
            val posX = position.first
            val posY = position.second
            val modifiedGrid = initialGrid.map { it.clone() }.toTypedArray()
            if (modifiedGrid[posY][posX] == '.') {
                modifiedGrid[posY][posX] = '#'
            }
            val (_, isCycle) = simulateGuardPath(modifiedGrid)
            if (isCycle) {
                cycleCount++
            }
        }
        return cycleCount
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
