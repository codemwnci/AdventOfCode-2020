import java.io.File

fun main() {
    Day13().puzzle1()
    Day13().puzzle2()
}

class Day13 {
    private val file = File("inputs/day13.txt")

    fun puzzle1() {
        val earliestDepart = file.readLines().first().toInt()
        val buses = file.readLines().last().split(",").filterNot { it=="x" }.map { it.toInt() }

        // create a map of busId (key), next departure (value)...then select the one with the lowest wait time
        // earliest % busId will tell us how long ago last one left, so subtract that from busId for when it arrives next
        buses.associateWith { it - earliestDepart % it }.minBy { it.value }.also {
            println(it)
            println(it.key * it.value) // finally multiply busID to wait time
        }
    }

    fun puzzle2() {
        data class Bus(val bus:Int, val offset: Int)

        val buses = file.readLines().last().split(",").mapIndexedNotNull { idx, bus -> if (bus=="x") null else Bus(bus.toInt(), idx) }

        // Solution 1 - using forEach
        var stepSize = 1L
        var time = 0L
        buses.forEach { (bus, offset) ->
            while ((time + offset) % bus != 0L) { time += stepSize }

            stepSize *= bus // we've found where this bus leaves 'offset' minutes after the first bus. need to update step for next
                            // bus, so it is the product of all previous bus times
        }
        println(time)

        // Solution 2 - using Fold
        buses.fold(Pair(1L, 0L)) { (step, prevStepsTime), (bus, offset) ->
            var t = prevStepsTime
            while ((t + offset) % bus != 0L) { t += step }

            Pair(step * bus, t)
        }.also { println(it.second) }
    }
}