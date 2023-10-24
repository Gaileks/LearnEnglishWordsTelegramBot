import java.io.File

data class Statistics(
    val learned: Int,
    val total: Int,
    val percent: Int,
)

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)

class LearnWordsTrainer(
    private val wordFileName: String = "words.txt",
    private val learnedAnswerCount: Int = 3,
    private val countOfQuestionWords: Int = 4
) {
    var dictionary = loadDictionary()

    fun getStatistics(): Statistics {
        val learned = dictionary.filter { it.correctAnswersCount >= learnedAnswerCount }.size
        val total = dictionary.size
        val percent = learned * 100 / total
        return Statistics(learned, total, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < learnedAnswerCount }
        if (notLearnedList.isEmpty()) return null
        val questionWords = if (notLearnedList.size < countOfQuestionWords) {
            val learnedList = dictionary.filter { it.correctAnswersCount >= learnedAnswerCount }.shuffled()
            notLearnedList.shuffled().take(countOfQuestionWords) +
                    learnedList.take(countOfQuestionWords - notLearnedList.size)
        } else {
            notLearnedList.shuffled().take(countOfQuestionWords)
        }.shuffled()
        val correctAnswer = questionWords.random()
        return Question(variants = questionWords, correctAnswer = correctAnswer)
    }

    fun resetProgress(): List<Word> {
        val dictionary = this.dictionary.map {
            Word(it.questionWord, it.translate, 0)
        }
        writingToFile(dictionary)
        return dictionary
    }

    fun writingToFile(dictionary: List<Word>) {
        val wordFile = File(wordFileName)
        val textToWriting = dictionary.joinToString("") {
            "${it.questionWord}|${it.translate}|${it.correctAnswersCount}\n"
        }
        wordFile.writeText(textToWriting)
    }

    private fun loadDictionary(): List<Word> {
        return File(wordFileName)
            .readLines()
            .mapNotNull { it ->
                val line = it.split("|").filter { it.isNotEmpty() }
                if (line.size == 3) {
                    Word(
                        line.getOrNull(0) ?: return@mapNotNull null,
                        line.getOrNull(1) ?: return@mapNotNull null,
                        line[2].toIntOrNull() ?: return@mapNotNull null
                    )
                } else null
            }
    }
}