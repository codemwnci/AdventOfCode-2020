import java.io.File

fun main() {
    Day17().puzzle1()
    Day17().puzzle2()
}

class Day17 {
    private val file = File("inputs/day17.txt")

    private data class Point4D(val x:Int, val y:Int, val z:Int = 0, val w:Int = 0) {
        fun countNeighbours(allPoints: List<Point4D>) = allPoints.count {
            it != this &&
            it.x in this.x-1 .. this.x+1 &&
            it.y in this.y-1 .. this.y+1 &&
            it.z in this.z-1 .. this.z+1 &&
            it.w in this.w-1 .. this.w+1
        }
    }

    private fun List<Point4D>.extendedBounds(): List<IntRange> =
        listOf(
            this.minOf { it.x } - 1 .. this.maxOf { it.x } + 1,
            this.minOf { it.y } - 1 .. this.maxOf { it.y } + 1,
            this.minOf { it.z } - 1 .. this.maxOf { it.z } + 1,
            this.minOf { it.w } - 1 .. this.maxOf { it.w } + 1
        )
    private val activePoints = file.readLines().flatMapIndexed { y, row -> row.mapIndexed { x, c -> if (c == '#') Point4D(x, y) else null } }.filterNotNull()

    fun puzzle1() {
        println(cycle(activePoints, 6).size)
    }
    fun puzzle2() {
        println(cycle(activePoints, 6, 4).size)
    }

    private fun cycle(activePoints: List<Point4D>, cyclesRemaining: Int, dimensions:Int = 3): List<Point4D> {

        val newPoints = mutableListOf<Point4D>()

        // rule 1 - only stay active if exactly 2 or 3 active neighbours
        newPoints += activePoints.filter { it.countNeighbours(activePoints) in 2..3 }

        // rule 2 - become active if not currently active
        val bounds = activePoints.extendedBounds()
        val (xBound, yBound, zBound) = bounds
        val wBound = if (dimensions == 4) bounds[3] else 0..0

        newPoints += sequence { xBound.forEach { x -> yBound.forEach { y-> zBound.forEach { z-> wBound.forEach { w->
           val p = Point4D(x, y, z, w)
           if (p !in activePoints && p.countNeighbours(activePoints) == 3) yield(p)
        }}}}}

        return if (cyclesRemaining == 1) newPoints else cycle(newPoints, cyclesRemaining-1, dimensions)
    }
}