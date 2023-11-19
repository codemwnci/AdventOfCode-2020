import java.io.File

fun main() {
    Day02().puzzle1()
    Day02().puzzle2()
}

class Day02 {
    private val file = File("inputs/day02.txt")

    data class PassPolicy(val pass:String, val c: Char, val range:IntRange)

    fun puzzle1() {

        // solution 1 - using multiple splits to break the string down
        val passwords = file.readLines().map {
            it.split(": ").let {
                val pass = it[1]
                it[0].split(" ").let {
                    val policyChar = it[1].first()
                    it[0].split("-").let {
                        val minMax = it[0].toInt() .. it[1].toInt()

                        PassPolicy(pass, policyChar, minMax)
                    }
                }
            }
        }

        println(passwords.count {  it.pass.count { ch -> ch == it.c} in it.range })
        // 445

        // solution 2 - using Regex groups
        val regex = """(?<min>[0-9]+)-(?<max>[0-9]+)\s(?<pchar>[a-z]):\s(?<pass>[a-z]+)""".toRegex()
        val passwords2 = file.readLines().map {
            val match = regex.find(it)!!
            PassPolicy(match.groups["pass"]?.value!!,
                       match.groups["pchar"]?.value!!.first(),
                       match.groups["min"]?.value!!.toInt() .. match.groups["max"]?.value!!.toInt())
        }

        println(passwords2.count {  it.pass.count { ch -> ch == it.c} in it.range })

    }

    fun puzzle2() {

        // just use regex solution from puzzle 1
        val regex = """(?<first>[0-9]+)-(?<second>[0-9]+)\s(?<pchar>[a-z]):\s(?<pass>[a-z]+)""".toRegex()
        val passwords = file.readLines().map {
            val match = regex.find(it)!!

            val pass = match.groups["pass"]?.value!!
            val c = match.groups["pchar"]?.value!!.first()

            // map to true / false if one or other characters (XOR) equals the password character
            (pass[match.groups["first"]?.value!!.toInt() -1] == c) xor (pass[match.groups["second"]?.value!!.toInt() -1] == c)
        }

        println(passwords.count { it })
    }
}