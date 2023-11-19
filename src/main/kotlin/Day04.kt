import java.io.File

fun main() {
    Day04().puzzle1()
    Day04().puzzle2()
}

class Day04 {
    private val file = File("inputs/day04.txt")

    fun puzzle1() {
        val ids = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid") // not including cid

        // read all lines, split each passport on a blank line, then join each multiline passport onto a single String
        file.readLines().split { it.isBlank() }.map { it.joinToString(" ") }.count { passport ->
            // check if all 7 ids are contained in the passport string
            ids.all { passport.contains("$it:") }
        }.also { println(it) }
    }

    fun puzzle2() {
        val ids = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid") // not including cid
        val ecls = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")

        file.readLines().split { it.isBlank() }.map { it.joinToString(" ") }.filter { passport ->
            ids.all { passport.contains("$it:") }
        }.count {
            // split by space to get all field/value pairs, and make sure all fields are valid
            it.split(" ").all {
                val (field, value) = it.split(":") // then split by field and value and deconstruct

                when(field) {
                    "byr" -> value.toInt() in 1920 .. 2002
                    "iyr" -> value.toInt() in 2010 .. 2020
                    "eyr" -> value.toInt() in 2020 .. 2030
                    "hgt" -> {
                        val num = value.dropLast(2).toIntOrNull() ?: 0
                        (value.endsWith("in") && num in 59 .. 76) || (value.endsWith("cm") && num in 150 .. 193)
                    }
                    "hcl" -> Regex("#([A-Fa-f0-9]){6}").matches(value)
                    "ecl" -> ecls.contains(value)
                    "pid" -> Regex("([0-9]){9}").matches(value)
                    else -> true// ignore cid and unexpected fields
                }
            }
        }.also { println(it) }
    }
}