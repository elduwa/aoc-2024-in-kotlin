
fun main() {

    data class Node(val page : Int, val dependentNodes : MutableSet<Int> = mutableSetOf() , var incomingEdges : Int = 0, var visited : Boolean = false)

    fun parseInput(input : List<String>) : Pair<Map<Int, Node>, List<List<Int>>> {
        val graph = mutableMapOf<Int, Node>()
        //val updates = mutableListOf<List<Int>>()

        val dependencies = input.filter { it.contains("|") }
            .map { line -> line.trim().split("|").toSet().map { it.toInt() } }
            .groupBy({ it[0] }, { it[1] })

        val updates = input.filter { it.contains(",") }
            .map { line -> line.trim().split(",").map { it.toInt() } }

        for ((k, v) in dependencies) {
            val startNode = graph.getOrPut(k) { Node(k) }
            val endNodes = v.map { graph.getOrPut(it) { Node(it) } }
            endNodes.forEach { node -> node.incomingEdges += 1 }
            startNode.dependentNodes.addAll(endNodes.map { it.page })
        }

        return graph to updates
    }

    fun part1(input : List<String>): Int {
        val (graph, updates) = parseInput(input)

        var result = 0
        for (update in updates) {
            graph.values.map { it.visited = false }
            var isCorrectOrder = true
            for (page in update) {
                val node = graph[page]
                if (node == null || node.dependentNodes.any { graph[it]?.visited == true }) {
                    isCorrectOrder = false
                    break
                }
                node.visited = true
            }
            if (isCorrectOrder) result += update[update.size.floorDiv(2)]
        }

        return result
    }

    fun part2(input : List<String>): Int {

        return 0
    }

    // Test if implementation meets criteria from the description, like:
    // check(part1(listOf("test_input")) == 1)

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    //check(part2(testInput) == 9)

    val input = readInput("Day05")
    part1(input).println()
    //part2(input).println()
}
