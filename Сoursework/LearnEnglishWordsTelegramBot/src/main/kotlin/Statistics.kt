package coursework

import kotlin.math.roundToInt

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
        println("Выучено $numberWordsLearned из $numberWordsInDictionary слов | ${(percentageRatio * 100).roundToInt() / 100.0}%")
    }
}
