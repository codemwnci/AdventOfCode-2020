import java.io.File

fun main() {
    Day05().puzzle1()
    Day05().puzzle2()
}

class Day05 {
    private val file = File("inputs/day05.txt")

    fun puzzle1() {
        file.readLines().map {
            val row = it.take(7).replace("F", "0").replace("B", "1").toInt(2)
            val col = it.takeLast(3).replace("L", "0").replace("R", "1").toInt(2)

            row * 8 + col
        }
        .max()
        .also { println(it) }
    }

    fun puzzle2() {
        file.readLines().map {
            val row = it.take(7).replace("F", "0").replace("B", "1").toInt(2)
            val col = it.takeLast(3).replace("L", "0").replace("R", "1").toInt(2)

            row * 8 + col
        }
        .sorted()
        .windowed(2, 1).first { it[1] - it[0] == 2 }
        .also { println(it[0] + 1) }
    }
}