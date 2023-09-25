package coursework

class UploadSaveData {

    fun loadDictionary(): MutableList<Word> {
        val dictionary: MutableList<Word> = mutableListOf()
        val wordFile = WordFile()
        for (lineFile in wordFile.linesFile) {
            val line = lineFile.split("|")
            dictionary.add(Word(line[0], line[1], line[2].toInt()))
        }
        return dictionary
    }

    fun saveData(textToWriting: String) {
        val file = WordFile()
        file.writeTextToFile(textToWriting)
    }
}