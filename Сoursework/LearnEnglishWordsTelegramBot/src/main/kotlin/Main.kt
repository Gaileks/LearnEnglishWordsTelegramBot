package coursework

import java.io.File

fun main() {
    val wordFile = File("words.txt")
//    wordFile.writeText(
//        """
//        |hello привет
//        |dog собака
//        |cat кошка
//    """.trimMargin()
//    )
    wordFile.readLines().forEach {
        println(it)
    }
}
