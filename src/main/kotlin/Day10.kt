import java.io.File

fun main() {
    Day10().puzzle1()
    Day10().puzzle2()
}

class Day10 {
    private val file = File("inputs/day10.txt")

    private val adapterList = (listOf(0) + file.readLines().map { it.toInt() }).sorted().let { it + listOf(it.last()+3) }

    fun puzzle1() {
        adapterList.zipWithNext { a, b -> b - a }.groupBy { it }.also { println(it[1]!!.count() * it[3]!!.count()) }
    }

    fun puzzle2() {
        println(adapterList)

        val prevPaths = mutableMapOf<Int, Long>(0 to 1).withDefault { 0 }
        for(adapter in adapterList.drop(1)) {
            // ways to get to this adapter is the sum of the options before it
            prevPaths[adapter] = prevPaths.filter { it.key in adapter-3 until adapter }.map { it.value }.sum()
        }
        println(prevPaths)
        println(prevPaths[prevPaths.keys.maxOrNull()!!]!!)

    }
}