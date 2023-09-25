package coursework

import java.io.File

class WordFile {
    private val wordFile = File("words.txt")
    val linesFile = wordFile.readLines()

    fun writeTextToFile(textToWriting: String) {
        wordFile.writeText(textToWriting)
    }
}
