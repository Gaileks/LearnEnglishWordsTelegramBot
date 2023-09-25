package coursework

import kotlin.math.roundToInt

class Statistics {
    private val numberWordsInDictionary = UploadSaveData().loadDictionary().size
    private val numberWordsLearned = getNumberWordsLearned()
    private val percentageRatio = getPercentageRatio()

    fun getStatisticsInfo() {
        val dataInputOutput = DataInputOutput()
        dataInputOutput.dataOutput("Выучено $numberWordsLearned из $numberWordsInDictionary слов | ${(percentageRatio * 100).roundToInt() / 100.0}%")
    }

    private fun getNumberWordsLearned(): Int {
        var counter = 0
        UploadSaveData().loadDictionary().forEach() {
            if (it.correctAnswersCount >= REPETITIONS_TO_MEMORIZE) counter++
        }
        return counter
    }

    private fun getPercentageRatio(): Double = (numberWordsLearned.toDouble() / numberWordsInDictionary) * 100
}
