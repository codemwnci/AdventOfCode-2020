import java.io.File

fun main() {
    Day09().puzzle1()
    Day09().puzzle2()
}

class Day09 {
    private val file = File("inputs/day09.txt")

    fun puzzle1() {
        file.readLines().map { it.toLong() }.windowed(26, 1).first {
            val num = it.last()
            val preamble = it.dropLast(1)

            val isValid = preamble.combinations(2).any { it.sum() == num }

            !isValid
        }
        .also { println(it.last()) }
    }

    fun puzzle2() {
        val target = 675280050L

        val input = file.readLines().map { it.toLong() }

        for (i in 2 until input.size) {
            val found = input.windowed(i, 1).find {
                it.sum() == target
            }

            if (found != null) {
                println(found)
                println(found.min() + found.max())
                break
            }
        }
    }
}