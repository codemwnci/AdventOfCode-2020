import java.io.File

fun main() {
    Day11().puzzle1()
    Day11().puzzle2()
}

class Day11 {
    private val file = File("inputs/day11.txt")
    private val initialGrid = file.readLines().map { it.toCharArray().toMutableList() }

    private fun neighbours(searchGrid: List<List<Char>>, x: Int, y: Int): List<Char> =
        buildList {
            (y - 1..y + 1).forEach { y1 ->
                (x - 1..x + 1).forEach { x1 ->
                    // exclude self (y, x) and don't go outside the boundary
                    if (!(x == x1 && y == y1) && (x1 in searchGrid[0].indices && y1 in searchGrid.indices)) add(searchGrid[y1][x1])
                }
            }
        }

    private fun visibleNeighbours(searchGrid: List<List<Char>>, x:Int, y:Int, notVisibleList: List<Char> = listOf('.')): List<Char> =
        buildList {
            (-1 .. 1).forEach { yStep ->
                (-1 .. 1).forEach { xStep ->
                    if (!(yStep == 0 && xStep == 0)) {
                        val maxBurst = Math.max(searchGrid.size, searchGrid[0].size)
                        (1..maxBurst).firstOrNull { burst ->
                            val x1 = x + xStep*burst
                            val y1 = y + yStep*burst

                            x1 in searchGrid[0].indices &&
                                y1 in searchGrid.indices &&
                                !notVisibleList.contains(searchGrid[y1][x1])
                        }?.let {
                            val x1 = x + xStep*it
                            val y1 = y + yStep*it
                            add(searchGrid[y1][x1])
                        }
                    }
                }
            }
        }

    private fun printGrid(newGrid: List<List<Char>>) {
        newGrid.forEach {
            println()
            it.forEach(::print)
        }
    }

    fun puzzle1() {
        fun simulateSingleRound(grid: List<List<Char>>): List<List<Char>> {
            val newGrid = grid.mapIndexed{ yIdx, xList -> xList.mapIndexed { xIdx, c ->
                val neighbours = neighbours(grid, xIdx, yIdx)
                if (c == 'L' && neighbours.count { it=='#' } == 0) '#'
                else if (c == '#' && neighbours.count { it=='#' } >= 4) 'L'
                else c
            }}

            return if (newGrid == grid) newGrid
            else simulateSingleRound(newGrid)
        }

        println(simulateSingleRound(initialGrid).flatten().count { it == '#' })
    }

    fun puzzle2() {
        fun simulateSingleRound(grid: List<List<Char>>): List<List<Char>> {
            val newGrid = grid.mapIndexed{ yIdx, xList -> xList.mapIndexed { xIdx, c ->
                val neighbours = visibleNeighbours(grid, xIdx, yIdx)
                if (c == 'L' && neighbours.count { it=='#' } == 0) '#'
                else if (c == '#' && neighbours.count { it=='#' } >= 5) 'L'
                else c
            }}

            return if (newGrid == grid) newGrid
            else simulateSingleRound(newGrid)
        }

        println(simulateSingleRound(initialGrid).flatten().count { it == '#' })
    }
}