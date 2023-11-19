import java.io.File

fun main() {
    Day14().puzzle1()
    Day14().puzzle2()
}

class Day14 {
    private val file = File("inputs/day14.txt")

    // split lines at "mask" but don't discard the line split on
    private val input = file.readLines().fold(mutableListOf<MutableList<String>>()) { acc, str ->
        if (str.startsWith("mask")) acc.add(mutableListOf(str))
        else acc.last().add(str)
        acc
    }

    fun puzzle1() {
        val memStore = HashMap<Int, Long>()
        input.forEach { lines ->
            val mask = lines.first().substringAfter("mask = ")
            lines.drop(1).forEach {
                val mem = it.substringAfter("mem[").substringBefore("]").toInt()
                val maskedResult = it.substringAfter("= ").toLong().toString(2).padStart(36, '0').mapIndexed { idx, c ->
                    when (mask[idx]) {
                        '1' -> '1'
                        '0' -> '0'
                        else -> c
                    }
                }
                memStore[mem] = maskedResult.joinToString("").toLong(2)
            }
        }

        println(memStore)
        println(memStore.values.sum())
    }

    fun puzzle2() {
        val memStore = HashMap<Long, Long>()

        input.forEach { lines ->
            val mask = lines.first().substringAfter("mask = ")
            lines.drop(1).forEach {
                val memValue = it.substringAfter("= ").toLong()
                val maskPass1 = it.substringAfter("mem[").substringBefore("]").toLong().toString(2).padStart(36, '0').mapIndexed { idx, c ->
                    when (mask[idx]) {
                        '1' -> '1'
                        'X' -> 'X'
                        else -> c// this can only be 0 which is left unchanges
                    }
                }

                val queue = mutableListOf(maskPass1)
                while (queue.isNotEmpty()) {
                    val maskPass2 = queue.removeFirst()
                    val xIdx = maskPass2.indexOf('X')
                    if (xIdx >= 0) { // there are still Xs to replace
                        queue.add(maskPass2.toMutableList().also {it[xIdx] = '0'})
                        queue.add(maskPass2.toMutableList().also {it[xIdx] = '1'})
                    }
                    else {
                        // this is now a complete memory address (all interim Xs are removed
                        val memAddr = maskPass2.joinToString("").toLong(2)
                        memStore[memAddr] = memValue
                    }
                }
            }
        }

        println(memStore.values.sum())
    }
}