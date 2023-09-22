package coursework

import java.io.File

class WordFile {
    val wordFile = File("words.txt")
    var dictionary: MutableList<Word> = mutableListOf()
    private val linesFile = wordFile.readLines()

    init {
        addWordToDictionary()
    }

    private fun addWordToDictionary() {
        for (lineFile in linesFile) {
            val line = lineFile.split("|")
            dictionary.add(Word(line[0], line[1], line[2].toInt()))
        }
    }

    fun resetProgress() {
        val textToWriting = WordFile().dictionary.joinToString("") {
            "${it.text}|${it.translate}|${0}\n"
        }
        WordFile().wordFile.writeText(textToWriting)
    }

    fun writingToFile(wordFile: WordFile) {
        val textToWriting = wordFile.dictionary.joinToString("") {
            "${it.text}|${it.translate}|${it.correctAnswersCount}\n"
        }
        wordFile.wordFile.writeText(textToWriting)
    }
}
