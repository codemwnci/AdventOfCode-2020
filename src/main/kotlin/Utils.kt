import java.util.*
import kotlin.collections.ArrayDeque

// Helper functions to make ArrayDeque look more like a Stack
fun <T> ArrayDeque<T>.push(element: T) = addLast(element)
fun <T> ArrayDeque<T>.pop() = removeLastOrNull()
fun <T> ArrayDeque<T>.peek() = lastOrNull()

fun <T> priorityQueueOf(vararg args: T): PriorityQueue<T> = PriorityQueue<T>().also { it.addAll(args) }

fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> = fold(mutableListOf(mutableListOf<T>())) { acc, t ->
    if (predicate(t)) acc.add(mutableListOf())
    else acc.last().add(t)
    acc
}

fun <T> List<T>.combinations(size: Int): List<List<T>> = when (size) {
    0 -> listOf(listOf())
    else -> flatMapIndexed { idx, element -> drop(idx + 1).combinations(size - 1).map { listOf(element) + it } }
}

//fun main() {
//
//    val page = StringBuffer()
//    page.append("<html><head><title>Advent Of Code - 2020</title></head><body>")
//    (1..25).forEach {
//        page.append("<h1>Day $it</h1>")
//        val p = java.net.URL("https://adventofcode.com/2020/day/$it").readText()
//        val main = p.substring(p.indexOf("<main>"), p.indexOf("</main>")+7)
//        page.append(main)
//    }
//    page.append("</body></html>")
//
//    java.io.File("puzzles.html").writeText(page.toString())
//
//}

//fun main() {
//    val template = java.io.File("src/main/kotlin/Template.kt").readText()
//
//    (1..25).forEach {
//        val day = it.toString().padStart(2, '0')
//
//        // create the input blank text file
//        java.io.File("inputs/day${day}.txt").createNewFile()
//
//        // create the Kotlin class, based on the template, replacing XX with the actual Day
//        val f = java.io.File("src/main/kotlin/Day${day}.kt")
//        f.writeText(template.replace("XX", day))
//    }
//}

//fun main() {
//    val cookie = ""
//    (1..25).forEach {
//        val day = it.toString().padStart(2, '0')
//
//        val http = java.net.URL("https://adventofcode.com/2020/day/$it/input").openConnection()
//        http.addRequestProperty("Cookie", "session=$cookie")
//
//        java.io.File("inputs/day${day}.txt").writeText(InputStreamReader(http.getInputStream()).readText())
//    }
//}