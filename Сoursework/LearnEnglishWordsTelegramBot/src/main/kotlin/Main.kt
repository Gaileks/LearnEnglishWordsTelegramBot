package coursework

import java.io.File

fun main() {
    val wordFile = File("words.txt")
    val dictionary: MutableList<Word> = mutableListOf()
    val linesFile = wordFile.readLines()

    addWordToDictionary(linesFile, dictionary)

    dictionary.forEach {
        println(it)
    }
}

private fun addWordToDictionary(linesFile: List<String>, dictionary: MutableList<Word>) {
    for (lineFile in linesFile) {
        val line = lineFile.split("|")
        dictionary.add(Word(line[0], line[1], line[2].toInt()))
    }
}

data class Word(
        private val text: String,
        private val translate: String,
        private val correctAnswersCount: Int = 0,
) {
    override fun toString(): String {
        return "Слово: $text, Перевод: $translate, Правильных ответов: $correctAnswersCount"
    }
}