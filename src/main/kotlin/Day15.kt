import java.io.File

fun main() {
    Day15().puzzle1()
    Day15().puzzle2()
}

class Day15 {
    private val file = File("inputs/day15.txt")
    private val initSeq = file.readText().split(",").map { it.toInt() }

    fun puzzle1() {
        playGame(initSeq, 2020).also { println(it) }
    }

    fun puzzle2() {
        playGame(initSeq, 30000000).also { println(it) }
    }

    private fun playGame(initSeq: List<Int>, playUntil: Int): Int {
        var step = 0
        val visited = IntArray(playUntil+1)
        var lastSpoken = -1
        initSeq.forEach {
            step++
            visited[it] = step
            lastSpoken = it
        }

        repeat(playUntil - initSeq.size) {
            val prevTurn = visited[lastSpoken].let { if (it == 0) step else it }
            val number = step - prevTurn

            visited[lastSpoken] = step
            step++
            lastSpoken = number
        }
        return lastSpoken
    }
}