package coursework

import kotlin.system.exitProcess

class ProgramLogic {

    private fun getNotLearnedWord(): MutableList<Word> {
        val uploadSaveData = UploadSaveData()
        val notLearnedWord: MutableList<Word> = mutableListOf()
        uploadSaveData.loadDictionary().forEach() {
            if (it.correctAnswersCount < REPETITIONS_TO_MEMORIZE) notLearnedWord.add(it)
        }
        return notLearnedWord
    }

    fun listOfResponseOptions(): Pair<Word, List<Word>> =
        getNotLearnedWord().shuffled().take(4)
            .let { questionWords ->
                val uploadSaveData = UploadSaveData()
                val hiddenWord = questionWords.random()
                val words = if (questionWords.size < 4) {
                    val learnedWord = uploadSaveData.loadDictionary()
                        .filter { it.correctAnswersCount >= REPETITIONS_TO_MEMORIZE }
                    questionWords + learnedWord.shuffled()
                        .take(4 - questionWords.size)
                } else questionWords
                hiddenWord to words
            }

    fun checkingLearnedWords() {
        val dataInputOutput = DataInputOutput()
        if (getNotLearnedWord().isEmpty()) {
            dataInputOutput.dataOutput("Поздравляем, вы выучили все слова !!!".cyan())
            dataInputOutput.dataOutput("Спасибо, что пользовались нашей программой".cyan())
            exitProcess(0)
        }
    }

    fun checkingCorrectAnswer(hiddenWord: String, answerWord: String) {
        val dataInputOutput = DataInputOutput()
        val uploadSaveData = UploadSaveData()
        val dictionary = uploadSaveData.loadDictionary()
        if (hiddenWord == answerWord) {
            dataInputOutput.dataOutput("Вы правильно перевели слово".cyan())
            dictionary.map {
                if (it.translate == answerWord) {
                    it.correctAnswersCount++
                }
            }
            writingToFile(dictionary)
            getNotLearnedWord()
        } else {
            dataInputOutput.dataOutput("Не правильный ответ".red())
        }
    }

    fun resetProgress() {
        val uploadSaveData = UploadSaveData()
        val textToWriting = uploadSaveData.loadDictionary().joinToString("") {
            "${it.text}|${it.translate}|${0}\n"
        }
        uploadSaveData.saveData(textToWriting)
    }

    private fun writingToFile(dictionary: MutableList<Word>) {
        val uploadSaveData = UploadSaveData()
        val textToWriting = dictionary.joinToString("") {
            "${it.text}|${it.translate}|${it.correctAnswersCount}\n"
        }
        println(textToWriting)
        uploadSaveData.saveData(textToWriting)
    }
}