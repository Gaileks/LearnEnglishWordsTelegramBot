package coursework

import java.io.File

fun main() {

    ConsoleMenu().startTheMenu()
}

class ConsoleMenu {

    fun startTheMenu() {
        val menuItems = """
    (Введите пункт меню) 
    Меню: 
     1 – Учить слова
     2 – Статистика
     0 – Выход    
""".trimIndent()

        while (true) {
            println(menuItems)
            when (checkingInput()) {
                1 -> println("Учим слова")
                2 -> Statistics().getStatisticsInfo()
                0 -> {
                    println("Спасио что пользовались нашей программой")
                    return
                }

                else -> println("Не корректный номер пункта меню".red())

            }
        }
    }

    private fun checkingInput(): Int {
        var number: Int?
        do {
            number = readln().toIntOrNull()
            if (number == null) println("Введенное значение не удовлетворяет требованиям".red())
        } while (number == null)
        return number
    }
}

class Statistics {
    private val numberWordsInDictionary = WordFile().dictionary.size
    private val numberWordsLearned = getNumberWordsLearned()
    private val percentageRatio = getPercentageRatio()

    private fun getNumberWordsLearned(): Int {
        var counter = 0
        WordFile().dictionary.forEach() {
            if (it.correctAnswersCount >= 3) counter++
        }
        return counter
    }

    private fun getPercentageRatio(): Double = (numberWordsLearned.toDouble() / numberWordsInDictionary) * 100

    fun getStatisticsInfo() {
        println("Выучено $numberWordsLearned из $numberWordsInDictionary слов | $percentageRatio%")
    }
}

class WordFile {
    private val wordFile = File("words.txt")
    val dictionary: MutableList<Word> = mutableListOf()
    private val linesFile = wordFile.readLines()

    init {
        addWordToDictionary()
    }

    fun getInfo() {
        dictionary.forEach {
            println(it)
        }
    }

    private fun addWordToDictionary() {
        for (lineFile in linesFile) {
            val line = lineFile.split("|")
            dictionary.add(Word(line[0], line[1], line[2].toInt()))
        }
    }
}

data class Word(
        private val text: String,
        private val translate: String,
        val correctAnswersCount: Int = 0,
) {
    override fun toString(): String {
        return "Слово: $text, Перевод: $translate, Правильных ответов: $correctAnswersCount"
    }
}
fun String.red() = "\u001b[31m${this}\u001b[0m"
fun String.cyan() = "\u001b[36m${this}\u001b[0m"