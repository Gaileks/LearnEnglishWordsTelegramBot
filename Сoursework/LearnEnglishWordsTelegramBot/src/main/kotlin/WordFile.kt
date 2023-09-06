package coursework

import java.io.File

class WordFile {
     val wordFile = File("words.txt")
    val dictionary: MutableList<Word> = mutableListOf()
    val linesFile = wordFile.readLines()

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
