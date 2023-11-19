import java.io.File

fun main() {
    Day08().puzzle1()
    Day08().puzzle2()
}

class Day08 {
    private val file = File("inputs/day08.txt")
    data class Op(val op:String, val num:Int)

    private fun runCode(ops: List<Op>): Pair<Int, Boolean> {

        val visited = mutableListOf<Int>()
        var step = 0
        var acc = 0
        while (!visited.contains(step) && step != ops.size && step >= 0) {
            visited.add(step)
            when (ops[step].op) {
                "nop" -> step++
                "jmp" -> step += ops[step].num
                "acc" -> {
                    acc += ops[step].num
                    step++
                }
            }
        }

        return Pair(acc, step == ops.size)
    }

    private val ops = file.readLines().map { Op(it.take(3), it.drop(4).toInt()) }

    fun puzzle1() {
        println(runCode(ops).first)
    }

    fun puzzle2() {

        for (i in ops.indices) {
            val modifiedCode = ops.toMutableList() // clone list
            if (modifiedCode[i].op != "acc") {
                modifiedCode[i] = Op(if (ops[i].op == "jmp") "nop" else "jmp", ops[i].num)
                val res = runCode(modifiedCode)
                if (res.second) {
                    println(res.first)
                    break
                }
            }
        }
    }
}