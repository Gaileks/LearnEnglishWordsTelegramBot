package coursework

import java.io.File

class FileDictionaryDataStore(
    wordFileName: String = "words.txt",
) {
    private val wordFile = File(wordFileName)
    val dictionary: List<Word> = loadDictionary()

    fun loadDictionary(): MutableList<Word> {
        val dictionary: MutableList<Word> = mutableListOf()
        val lines = wordFile.readLines()
        for (lineFile in lines) {
            val line = lineFile.split("|")
            dictionary.add(Word(line[0], line[1], line[2].toInt()))
        }
        return dictionary
    }

    fun saveData(textToWriting: String) {
        wordFile.writeText(textToWriting)
        loadDictionary()
    }

    fun resetProgress() {
        val textToWriting = dictionary.joinToString("") {
            "${it.text}|${it.translate}|${0}\n"
        }
        saveData(textToWriting)
        loadDictionary()
    }
}