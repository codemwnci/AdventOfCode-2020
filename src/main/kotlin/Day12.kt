import java.io.File
import kotlin.math.abs

fun main() {
    Day12().puzzle1()
    Day12().puzzle2()
}

class Day12 {
    private val file = File("inputs/day12.txt")
    private val instr = file.readLines().map { Nav(it.first(), it.drop(1).toInt()) }

    data class Nav(val action: Char, val num: Int)

    private fun changeDirection(currDir: Char, leftRight: Char, degrees: Int) : Char {
        val heading = when(currDir) {
            'N' -> 0
            'E' -> 90
            'S' -> 180
            'W' -> 270
            else -> 0
        }

        val turn = when(leftRight) {
            'L' -> degrees * -1
            else -> degrees
        }

        return when(heading + turn) {
            -360, 0, 360 -> 'N'
            -270, 90, 450 -> 'E'
            -180, 180, 540 -> 'S'
            -90, 270 -> 'W'
            else -> throw Exception("Unknown direction ${heading+turn}")
        }
    }

    fun puzzle1() {
        data class State(val ew:Int, val ns:Int, val dir: Char)

        fun updateState(state: State, nav: Nav): State {
            return when (nav.action) {
                'N' -> State(state.ew, state.ns + nav.num, state.dir)
                'S' -> State(state.ew, state.ns - nav.num, state.dir)
                'E' -> State(state.ew + nav.num, state.ns, state.dir)
                'W' -> State(state.ew - nav.num, state.ns, state.dir)
                'L' -> State(state.ew, state.ns, changeDirection(state.dir, 'L', nav.num))
                'R' -> State(state.ew, state.ns, changeDirection(state.dir, 'R', nav.num))
                'F' -> updateState(state, Nav(state.dir, nav.num)) // run this same calculation again, but with the action equivalent to the direction we are heading
                else -> throw Exception("Unknown navigation action -> ${nav.action}")
            }
        }

        instr.fold(State(0,0,'E')) { state, nav -> updateState(state, nav) }.also {
            println(it)
            println(abs(it.ew) + abs(it.ns))
        }
    }


    fun puzzle2() {
        data class Pos(val ew:Int, val ns:Int)
        data class State(val ship:Pos, val waypoint:Pos)

        fun rotateWaypoint(waypoint:Pos, nav:Nav): Pos {
            return when(nav.action.toString() + nav.num) {
                "L90", "R270" -> Pos(-waypoint.ns, waypoint.ew) // north becomes west (negative east), east becomes north
                "L180", "R180" -> Pos(-waypoint.ew, -waypoint.ns) // north becomes south (negative north), east becomes west (neg east)
                "L270", "R90" -> Pos(waypoint.ns, -waypoint.ew) // north becomes east, and east becomes south (negative north)
                else -> throw Exception("Unknown rotation")
            }

        }

        fun updateState(state: State, nav:Nav): State {
            return when (nav.action) {
                'N' -> State(state.ship, Pos(state.waypoint.ew, state.waypoint.ns + nav.num))
                'S' -> State(state.ship, Pos(state.waypoint.ew, state.waypoint.ns - nav.num))
                'E' -> State(state.ship, Pos(state.waypoint.ew + nav.num, state.waypoint.ns))
                'W' -> State(state.ship, Pos(state.waypoint.ew - nav.num, state.waypoint.ns))
                'L','R' -> State(state.ship, rotateWaypoint(state.waypoint, nav))
                'F' -> State(Pos(state.ship.ew + state.waypoint.ew * nav.num, state.ship.ns + state.waypoint.ns * nav.num), state.waypoint)
                else -> throw Exception("Unknown navigation action -> ${nav.action}")
            }
        }

        instr.fold(State(Pos(0,0), Pos(10,1))) {
            state, nav -> updateState(state, nav)
        }.also {
            println(it)
            println(abs(it.ship.ew) + abs(it.ship.ns))
        }
    }


}