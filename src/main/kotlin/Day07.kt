import java.io.File

fun main() {
    Day07().puzzle1()
    Day07().puzzle2()
}

class Day07 {
    private val file = File("inputs/day07.txt")

    data class Bags(val num:Int, val bag: String)
    data class Bag(val name: String, val bags: List<Bags>)

    private val bagList = file.readLines().map {
        Bag(it.substringBefore("bags contain").trim(),
            if (it.contains("no other bags")) emptyList()
            else it.substringAfter("bags contain").dropLast(1).split(",").map {
                val bagStr = it.trim()
                Bags(bagStr.substringBefore(" ").toInt(), bagStr.substringAfter(" ").substringBefore(" bag"))
            }
        )
    }

    private val bagMap = bagList.associateBy { it.name }

    fun puzzle1() {
        fun canContain(bagName:String, bag: Bag): Boolean {
            if (bag.bags.any { it.bag == bagName }) return true
            if (bag.bags.any { canContain(bagName, bagMap[it.bag]!!) }) return true

            return false
        }

        bagList.count { bag ->
            canContain("shiny gold", bag)
        }.also {println(it)}

    }

    fun puzzle2() {
        fun countBags(bagName: String) : Int {
            return bagMap[bagName]!!.bags.sumOf {
                it.num * (1 + countBags(it.bag))
            }
        }

        println(countBags("shiny gold"))
    }
}