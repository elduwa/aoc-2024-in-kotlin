fun main() {

    val memo = mutableMapOf<Pair<Long, Int>, Long>()

    fun countResultingStones(stone: Long, blinks: Int) : Long {
        val pair = stone to blinks
        var count = memo[pair]
        if (blinks == 0) {
            return 1
        }
        else if (count != null) {
            return count
        }
        else if (stone == 0L) {
            count = countResultingStones(1L, blinks - 1)
        }
        else if (stone.toString().length % 2 == 0) {
            val stoneString = stone.toString()
            val left = stoneString.slice(0..<stoneString.length / 2).toLong()
            val right = stoneString.slice(stoneString.length / 2..<stoneString.length).toLong()
            count = countResultingStones(left, blinks - 1) + countResultingStones(right, blinks - 1)
        }
        else {
            count = countResultingStones(stone * 2024L, blinks - 1)
        }
        memo[pair] = count
        return count
    }

    fun part1(input: List<String>): Long {
        val stones = input[0].split(" ").map { it.toLong() }
        return stones.sumOf { countResultingStones(it, 25) }
    }

    fun part2(input: List<String>): Long {
        val stones = input[0].split(" ").map { it.toLong() }
        return stones.sumOf { countResultingStones(it, 75) }
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 55312L)
    //check(part2(testInput) == 81)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()

}
