import java.io.File

fun main() {
    Day06().puzzle1()
    Day06().puzzle2()
}

class Day06 {
    private val file = File("inputs/day06.txt")

    fun puzzle1() {
        file.readLines().split { it.isBlank() }.map { group ->
            group.reduce { acc, s -> acc + s.filterNot { acc.contains(it) } }.length
        }.sum().also { println(it) }
    }

    fun puzzle2() {
        file.readLines().split { it.isBlank() }.map { group ->
            group.reduce { acc, s -> s.filter { acc.contains(it) } }.length
        }.sum().also { println(it) }
    }
}