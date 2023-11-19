import java.io.File

fun main() {
    Day19().puzzle1()
    Day19().puzzle2()
}

class Day19 {
    private val file = File("inputs/day19.txt")

    private sealed class Rule {
        data class SubRules(val rules: List<Int>, val orRules:List<Int>?): Rule()
        data class Literal(val c: Char): Rule()
    }

    fun puzzle1() {
        val (rules, messages) = file.readLines().split { it.isBlank() }.let { (rulesRaw, messagesRaw) ->
            val rules = buildMap {
                rulesRaw.forEach {
                    val row = it.split(":", limit = 2)
                    this[row[0].toInt()] =
                        if (row[1].contains('"'))
                            Rule.Literal(row[1].trim()[1]) // after trim, skip over the " and take the character
                        else
                            row[1].split("|", limit = 2).map {
                                it.trim().split(" ").map { it.trim().toInt() }
                            }.let {
                                Rule.SubRules(it[0], it.getOrNull(1))
                            }
                }
            }
            rules to messagesRaw
        }

        val patterns = getPatterns(rules, 0)
        messages.count { patterns.contains(it) }.also { println(it) }
    }

    private fun getPatterns(rules: Map<Int, Rule>, startRule: Int) : List<String> {
        val patterns = mutableListOf<String>()
        val queue = ArrayDeque<Pair<String, List<Int>>>()
        queue.add("" to listOf(startRule))

        while (!queue.isEmpty()) {
            val (wipString, ruleList) = queue.removeFirst()

            val firstRule = rules[ruleList.first()] // work with one rule at a time
            if (firstRule is Rule.SubRules) {
                queue.addFirst(wipString to (firstRule.rules + ruleList.drop(1)))
                if (firstRule.orRules != null) {
                    queue.addFirst(wipString to (firstRule.orRules + ruleList.drop(1)))
                }
            }
            else if (firstRule is Rule.Literal) {
                if (ruleList.size > 1) {
                    // add this back to the queue, but with the Literal processed into the WIP String
                    // so we can continue to process the remaining rules (by removing the processed rule)
                    queue.addFirst((wipString+firstRule.c) to ruleList.drop(1))
                }
                else {
                    // no more to process, so add the pattern to the list
                    patterns.add(wipString + firstRule.c)
                }
            }
        }

        return patterns
    }

    fun puzzle2() {

    }
}