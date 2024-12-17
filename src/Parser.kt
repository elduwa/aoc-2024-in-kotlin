enum class TokenType(val regex : Regex) {
    NUMBER("""-?\d+(?:\.\d+)?""".toRegex()),
    WHITESPACE("""\s+""".toRegex()),
    PLUS("""\+""".toRegex()),
    MINUS("""-""".toRegex()),
    MULT("""\*""".toRegex()),
    CONCAT("""\|\|""".toRegex())
}

enum class NodeType () {
    BINARY,
    NUMBER
}

data class AbstractSyntaxTreeNode(val type : NodeType, val token : Pair<String, TokenType>?, val left : AbstractSyntaxTreeNode?, val right : AbstractSyntaxTreeNode?)

// Iterator returns matched token and token type
class Tokenizer(private val tokenTypes : List<TokenType>) : Iterator<Pair<String, TokenType>> {

    private var cursor = 0
    private var input = ""

    fun read(input : String) {
        cursor = 0
        this.input = input
    }

    override fun hasNext(): Boolean {
        return cursor < input.length
    }

    override fun next(): Pair<String, TokenType> {
        //val remainingInput = input.substring(cursor)
        val (token, type) = tokenTypes.map { it.regex.matchAt(input, cursor)?.value to it }.first { it.first != null }
        cursor += token!!.length

        if (type == TokenType.WHITESPACE) return next()

        return token to type
    }
}

/**
 * Grammar:
 *
 * EXPRESSION
 *     : ADDITION
 *     ;
 *
 * ADDITION
 *     : ADDITION ('+' | '-') MULT
 *     | MULTIPLICATION
 *     ;
 *
 * MULTIPLICATION
 *     : MULTIPLICATION ('*' | '/') BASIC
 *     | BASIC
 *     ;
 *
 * BASIC
 *     : number
 *     | '(' EXPRESSION ')'
 *     ;
 */
class ArithmeticExpressionParser(private val tokenizer: Tokenizer) {

    private var lookahead : Pair<String, TokenType>? = null

    fun read(input : String, usePrecedenceRules : Boolean = true): AbstractSyntaxTreeNode {
        tokenizer.read(input)
        lookahead = tokenizer.next()
        return expression(usePrecedenceRules)
    }

    private fun eat(vararg tokenTypes : TokenType): Pair<String, TokenType>? {
        val token = lookahead

        if (tokenTypes.none { it == token?.second }) {
            error("Expected ${token?.second} to be one of: $tokenTypes")
        }

        if (tokenizer.hasNext()) {
            lookahead = tokenizer.next()
        }
        else {
            lookahead = null
        }

        return token
    }

    private fun isType(vararg tokenTypes : TokenType) : Boolean {
        return tokenTypes.any { it == lookahead?.second }
    }

    private fun expression(usePrecedenceRules: Boolean) : AbstractSyntaxTreeNode {
       if (usePrecedenceRules) {
            return addition()
       }
       else {
           var left = basic()

           while (isType(TokenType.MULT, TokenType.PLUS, TokenType.MINUS, TokenType.CONCAT)) {
               left = AbstractSyntaxTreeNode(
                   NodeType.BINARY,
                   eat(TokenType.MULT, TokenType.PLUS, TokenType.MINUS, TokenType.CONCAT),
                   left,
                   basic()
               )
           }
           return left
       }
    }

    private fun addition(): AbstractSyntaxTreeNode {
        var left = multiplication()

        while (isType(TokenType.PLUS, TokenType.MINUS)) {
            left = AbstractSyntaxTreeNode(
                NodeType.BINARY,
                eat(TokenType.PLUS, TokenType.MINUS),
                left,
                multiplication()
            )
        }
        return left
    }

    private fun multiplication() : AbstractSyntaxTreeNode {
        var left = basic()

        while (isType(TokenType.MULT)) {
            left = AbstractSyntaxTreeNode(
                NodeType.BINARY,
                eat(TokenType.MULT),
                left,
                basic()
            )
        }
        return left
    }

    private fun basic() : AbstractSyntaxTreeNode {
        if (isType(TokenType.NUMBER)) {
            return AbstractSyntaxTreeNode(
                NodeType.NUMBER,
                eat(TokenType.NUMBER),
                null,
                null
            )
        }
        error("Malformed expression")
    }
}

fun binaryEval(operation : TokenType, left : Long, right: Long) : Long {
    return when (operation) {
        TokenType.PLUS -> left + right
        TokenType.MINUS -> left - right
        TokenType.MULT -> left * right
        TokenType.CONCAT -> (left.toString() + right.toString()).toLong()
        else -> error("Not a valid operation")
    }
}

fun evaluateNode(node : AbstractSyntaxTreeNode) : Long {
    return when (node.type) {
        NodeType.BINARY -> {
            val left = evaluateNode(node.left!!)
            val right = evaluateNode(node.right!!)
            binaryEval(node.token!!.second, left, right)
        }
        NodeType.NUMBER -> {
            node.token!!.first.toLong()
        }
    }
}