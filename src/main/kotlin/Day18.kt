import java.io.File

fun main() {
    Day18().puzzle1()
    Day18().puzzle2()
}

class Day18 {
    private val file = File("inputs/day18.txt")

    fun puzzle1() {
        file.readLines().sumOf(::parseAndCalculate).also { println(it) }
    }
    fun puzzle2() {
        file.readLines().sumOf{ parseAndCalculate(it, true) }.also { println(it) }
    }

    private fun parseAndCalculate(line: String, hasPrecedence: Boolean = false): Long {
        
        // the logic is to flatten the string by reducing the values in the brackets to calculated number
        // each open bracket will go into a new stack entry in case there are nested brackets
        // So,
        // if number or an operand +, * then add to the current stack
        // if ( create a new entry at the top of the stack (push)
        // if ) pop the current top entry, calculate it and add the result to the next item in the stack (the new top after the pop)

        val stack = ArrayDeque<MutableList<String>>()
        stack.push(mutableListOf()) // start with the base list, which we'll calculate against at the end

        // add spaces to brackets, so we can split by spaces, and then process one
        line.replace("(", "( ").replace(")", " )").split(" ").forEach {
            when(it) {
                "(" -> stack.push(mutableListOf())
                ")" -> {
                    val res = calculate(stack.pop(), hasPrecedence)
                    stack.peek()?.add(res.toString())
                }
                else -> stack.peek()?.add(it)
            }
        }
        // finally calculate
        return calculate(stack.pop(), hasPrecedence)
    }

    private fun calculate(items: MutableList<String>?, hasPrecedence: Boolean): Long {
        if (items == null) return 0

        if (!hasPrecedence) {
            // by dropping first, then chunking into 2, we will get 1 [+ 2] [+ 3] [+ 4]
            // which means we can fold, with the initial value, and then use the first part
            // of the chunk as the operand, and second part of chunk as the number
            // we then fold into the next value and continue due to no precendence
            return items.drop(1).chunked(2).fold(items[0].toLong()) { total, nextChunk ->
                when (nextChunk[0]) {
                    "+" -> total + nextChunk[1].toLong()
                    "*" -> total * nextChunk[1].toLong()
                    else -> throw Exception("Unexpected operand")
                }
            }
        }
        else {
            // this is not particularly elegant, and only works because there are two operands
            // allowing us to do a two-phase process to calculate all higher priority (+) operands first
            // before calculating the rest
            // This also only works because we have already flattened the string to remove all brackets.
            // Another hacky solution would have been to add brackets to the original string to make the precedence explicit

            // do a two phase pass. Phase one, just calculate +, phase two, calculate *
            val phase1 = items.drop(1).chunked(2).fold(listOf(items[0])) { reducedList, nextChunk ->
                when (nextChunk[0]) {
                    "+" -> reducedList.dropLast(1) + (reducedList.last().toLong() + nextChunk[1].toLong()).toString()
                    "*" -> reducedList + nextChunk
                    else -> throw Exception("Unexpected operand")
                }
            }
            return phase1.drop(1).chunked(2).fold(phase1[0].toLong()) { total, nextChunk ->
                when (nextChunk[0]) {
                    "*" -> total * nextChunk[1].toLong()
                    "+" -> throw Exception("Should already have calculated all pluses")
                    else -> throw Exception("Unexpected operand")
                }
            }
        }
    }
}
