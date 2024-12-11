
fun main() {

    data class Node(val page : Int, val dependentNodes : MutableSet<Int> = mutableSetOf() , var incomingEdges : Int = 0, var visited : Boolean = false)

    fun parseDependencies(input: List<String>, relevantPages : Set<Int>): Map<Int, List<Int>> {
        val dependencies = input.filter { it.contains("|") }
            .map { line -> line.trim().split("|").toSet().map { it.toInt() } }
            .filter { line -> line.all { relevantPages.contains(it) } }
            .groupBy({ it[0] }, { it[1] })
        return dependencies
    }

    fun parseUpdates(input: List<String>): List<List<Int>> {
        val updates = input.filter { it.contains(",") }
            .map { line -> line.trim().split(",").map { it.toInt() } }
        return updates
    }

    fun buildGraph(dependencies: Map<Int, List<Int>>): MutableMap<Int, Node> {
        val graph = mutableMapOf<Int, Node>()

        for ((k, v) in dependencies) {
            val startNode = graph.getOrPut(k) { Node(k) }
            val endNodes = v.map { graph.getOrPut(it) { Node(it) } }
            endNodes.forEach { node -> node.incomingEdges += 1 }
            startNode.dependentNodes.addAll(endNodes.map { it.page })
        }
        return graph
    }

    fun parseInput(input : List<String>) : Pair<Map<Int, Node>, List<List<Int>>> {
        val updates = parseUpdates(input)
        val dependencies = parseDependencies(input, updates.flatten().toSet())

        val graph = buildGraph(dependencies)

        return graph to updates
    }

    fun isCorrectOrder(graph: Map<Int, Node>, update: List<Int>): Boolean {
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
        graph.values.map { it.visited = false }
        return isCorrectOrder
    }

    fun part1(input : List<String>): Int {
        val (graph, updates) = parseInput(input)

        var result = 0
        for (update in updates) {
            val isCorrectOrder = isCorrectOrder(graph, update)
            if (isCorrectOrder) result += update[update.size.floorDiv(2)]
        }

        return result
    }

    fun part2(input : List<String>): Int {
        val updates = parseUpdates(input)

        var result = 0

        for (update in updates) {
            // parse relevant dependencies and build graph
            val dependencies = parseDependencies(input, update.toSet())
            val graph = buildGraph(dependencies)

            if (isCorrectOrder(graph, update)) continue

            // topological sorting
            val startingNodePages = graph.filter { (_, node) -> node.incomingEdges == 0 }.keys
            val pageQueue = ArrayDeque(startingNodePages)
            val topoSortedPages = mutableListOf<Int>()

            while (pageQueue.isNotEmpty()) {
                val currentNode = graph[pageQueue.removeFirst()] ?: break
                //"Current Node: ${currentNode.page}".println()
                currentNode.visited = true
                topoSortedPages.add(currentNode.page)

                currentNode.dependentNodes.forEach { page ->
                    val node = graph[page]
                    checkNotNull(node)
                    if (node.visited) error("The graph contains cycles. ${node.page} has already been visited.")
                    node.incomingEdges -= 1
                    if (node.incomingEdges == 0) pageQueue.addLast(node.page)
                }
            }
            result += topoSortedPages[topoSortedPages.size.floorDiv(2)]
        }
        return result
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
