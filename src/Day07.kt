fun main() {

    class ExpressionGenerator(val input: String, val operators: List<String>) : Iterator<String> {

        private val kTuple = mutableListOf<String>()

        override fun hasNext(): Boolean {
            return kTuple.isEmpty() || !kTuple.all { it == operators.last() }
        }

        override fun next(): String {
            if (kTuple.isEmpty()) {
                kTuple.addAll(MutableList(input.split("""\s+""".toRegex()).size - 1) { operators.first() })
            }
            else {
                var j = kTuple.size - 1
                while (j >= 0 && kTuple[j] == operators.last()) {
                    j -= 1
                }

                if (j >= 0) {
                    for (i in j..<kTuple.size) {
                        if (kTuple[i] == operators.last()) kTuple[i] = operators.first()
                        else {
                            val ind = operators.indexOf(kTuple[i])
                            kTuple[i] = operators[ind + 1]
                        }
                    }
                }
            }

            val iterator = kTuple.iterator()
            val builder = StringBuilder()
            for (number in input.split("""\s+""".toRegex())) {
                builder.append(number)
                if (iterator.hasNext()) {
                    builder.append(iterator.next())
                }
            }
            return builder.toString()
        }
    }

    fun parseAndEvaluate(tokenizer: Tokenizer, input: List<String>, operators : List<String>): Long {
        val parser = ArithmeticExpressionParser(tokenizer)

        var calibrationResult = 0L

        for (line in input) {
            val result = line.substringBefore(":").toLong()
            val factorString = line.substringAfter(": ")
            val generator = ExpressionGenerator(factorString, operators)
            while (generator.hasNext()) {
                val expression = generator.next()
                val root = parser.read(expression, false)
                if (evaluateNode(root) == result) {
                    calibrationResult += result
                    break
                }
            }
        }

        return calibrationResult
    }

    fun part1(input: List<String>): Long {
        val tokenizer = Tokenizer(TokenType.entries - TokenType.CONCAT)
        return parseAndEvaluate(tokenizer, input, listOf("+", "*"))
    }

    fun part2(input: List<String>): Long {
        val tokenizer = Tokenizer(TokenType.entries)
        return parseAndEvaluate(tokenizer, input, listOf("+", "*", "||"))
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
