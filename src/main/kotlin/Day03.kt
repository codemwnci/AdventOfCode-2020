import java.io.File

fun main() {
    Day03().puzzle1()
    Day03().puzzle2()
}

class Day03 {
    private val file = File("inputs/day03.txt")

    fun puzzle1() {
        val grid = file.readLines().map {it.toList() }

        var count = 0
        val gridWidth = grid[0].size
        for(i in 1 ..< grid.size) {
            if (grid[i][i * 3 % gridWidth] == '#') count++
        }

        println(count)
    }

    fun puzzle2() {

        val grid = file.readLines().map {it.toList() }

        fun countTrees(right: Int, down: Int): Long {
            var count = 0L
            val gridWidth = grid[0].size
            for(i in grid.indices step down) {
                if (grid[i][i / down * right % gridWidth] == '#') count++
            }

            return count
        }

        listOf(Pair(1, 1), Pair(3, 1), Pair(5, 1), Pair(7, 1), Pair(1, 2)).map {
            countTrees(it.first, it.second)
        }
        .fold(1L) { acc, i -> acc * i }.also { println(it) }
    }
}