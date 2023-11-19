import java.io.File

fun main() {
    Day01().puzzle1()
    Day01().puzzle2()
}

class Day01 {
    private val file = File("inputs/day01.txt")

    fun puzzle1() {
        val pair = file.readLines().map { it.toInt() }.combinations(2).first { it[0] + it[1] == 2020 }

        println(pair)
        println(pair[0] * pair[1])
        // 485739
    }

    fun puzzle2() {
        val triple = file.readLines().map { it.toInt() }.combinations(3).first { it.sum() == 2020 }
        println(triple)
        println(triple[0] * triple[1] * triple[2])
        // 161109702
    }
}