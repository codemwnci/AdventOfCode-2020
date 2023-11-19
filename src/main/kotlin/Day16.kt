import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    Day16().puzzle1()
    measureTimeMillis {
        Day16().puzzle2()
    }.also(::println)
}

class Day16 {
    private val file = File("inputs/day16.txt")

    // convert string in form Num-Num to IntRange
    private fun getRange(str: String) = str.split("-").let { it[0].toInt()..it[1].toInt() }

    fun puzzle1() {
        val (rules, _, otherTickets) = file.readLines().split { it.isBlank() }
        val ruleRanges = rules.flatMap  {
            listOf(
                getRange(it.substringAfter(": ").substringBefore(" or")),
                getRange(it.substringAfter("or "))
            )
        }

        otherTickets.drop(1).map {
            it.split(",").map{ it.toInt() }.firstOrNull {numToCheck ->
                ruleRanges.none { range -> numToCheck in range }
            } ?: 0
        }.sum().also(::println)
    }

    fun puzzle2() {
        data class Rule(val name: String, val range1: IntRange, val range2: IntRange)

        val (rulesStr, myTicketStr, otherTickets) = file.readLines().split { it.isBlank() }
        val allRules = rulesStr.map {
            Rule(it.substringBefore(":"),
                 getRange(it.substringAfter(": ").substringBefore(" or")),
                 getRange(it.substringAfter("or "))
            )
        }
        val ruleRanges = allRules.flatMap  { listOf(it.range1, it.range2) }

        val myTicket = myTicketStr[1].split(",").map { it.toInt() }
        val validTickets = otherTickets.drop(1).filter {
            it.split(",").map{ it.toInt() }.firstOrNull {numToCheck ->
                ruleRanges.none { range -> numToCheck in range }
            } == null
        }.map { it.split(",").map{ it.toInt() } }

        // this was quite tricky to begin with. Some rules are valid for multiple indexes (e.g. with my input, index 2 was valid
        // for all the departure rules). So, to solve, I keep looping through the rules, and only add them to the "ruleIndex" when there
        // is only a single option. The check for valid options checks the ranges, and that the index has not already been used.
        // this ensures that the indexes that have no other option (only a single option left) are set into the ruleIndex
        // and eventually all rules will be placed.
        // Each iteration of the while loop excludes rules that have already been found, so it gets progressively quicker.
        // This is unlikely to be the fastest solution, but still processes in under 100ms.

        val ruleIndex = mutableMapOf<String, Int>()
        while (ruleIndex.size != allRules.size) {
            allRules.filterNot { ruleIndex.contains(it.name) }.map { rule ->
                // convert each rule to the corresponding index
                (validTickets[0].indices).filter { idx ->
                    validTickets.all { !ruleIndex.containsValue(idx) && (it[idx] in rule.range1 || it[idx] in rule.range2) }
                }.also {
                    if (it.size == 1) {
                        ruleIndex[rule.name] = it[0]
                    }
                }
            }
        }

        ruleIndex.filter { it.key.contains("departure") }.toList()
            .fold(1L){ acc, i -> acc * myTicket[i.second] }
            .also(::println)
    }
}