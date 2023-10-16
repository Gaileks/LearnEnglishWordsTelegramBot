package coursework

import kotlin.math.roundToInt
import kotlin.system.exitProcess

class ProgramLogicStudyWords {
    private val fileDictionaryDataStore = FileDictionaryDataStore()
    private var dictionary = fileDictionaryDataStore.dictionary
    private val dataInputOutput = DataInputOutput()

    private fun getNotLearnedWord(): List<Word> {
        val notLearnedWord: MutableList<Word> = mutableListOf()
        dictionary.forEach() {
            if (it.correctAnswersCount < REPETITIONS_TO_MEMORIZE) notLearnedWord.add(it)
        }
        return notLearnedWord
    }

    fun listOfResponseOptions(): Pair<Word, List<Word>> =
        getNotLearnedWord().shuffled().take(4)
            .let { questionWords ->
                val hiddenWord = questionWords.random()
                val words = if (questionWords.size < 4) {
                    val learnedWord = dictionary
                        .filter { it.correctAnswersCount >= REPETITIONS_TO_MEMORIZE }
                    questionWords + learnedWord.shuffled()
                        .take(4 - questionWords.size)
                } else questionWords
                hiddenWord to words
            }

    fun checkingLearnedWords() {
        if (getNotLearnedWord().isEmpty()) {
            dataInputOutput.dataOutput("Поздравляем, вы выучили все слова !!!".cyan())
            dataInputOutput.dataOutput("Спасибо, что пользовались нашей программой".cyan())
            exitProcess(0)
        }
    }

    fun checkingCorrectAnswer(hiddenWord: String, answerWord: String) {
        val dictionary = dictionary
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
        fileDictionaryDataStore.resetProgress()
        this.dictionary = FileDictionaryDataStore().loadDictionary()
    }

    private fun writingToFile(dictionary: List<Word>) {
        val textToWriting = dictionary.joinToString("") {
            "${it.text}|${it.translate}|${it.correctAnswersCount}\n"
        }
        //println(textToWriting) проверка
        FileDictionaryDataStore().saveData(textToWriting)
    }

    fun getStatisticsInfo() {
        var numberWordsLearned = 0
        dictionary.forEach() { if (it.correctAnswersCount >= REPETITIONS_TO_MEMORIZE) numberWordsLearned++ }

        val percentageRatio = (numberWordsLearned.toDouble() / dictionary.size) * 100

        dataInputOutput.dataOutput("Выучено $numberWordsLearned из ${dictionary.size} слов | ${(percentageRatio * 100).roundToInt() / 100.0}%")
    }
}