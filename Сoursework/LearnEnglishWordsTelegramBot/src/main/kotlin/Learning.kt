package coursework

import kotlin.system.exitProcess

class Learning {
    private val notLearnedWord: MutableList<Word> = mutableListOf()

    init {
        addNotLearnedWord()
        checkingLearnedWords()
    }

    private fun addNotLearnedWord() {
        notLearnedWord.clear()
        WordFile().dictionary.forEach() {
            if (it.correctAnswersCount < REPETITIONS_TO_MEMORIZE) notLearnedWord.add(it)
        }
    }

    fun startLearningMenu() {
        while (true) {
            val wordListRestriction = 4
            val questionWords = notLearnedWord.shuffled().take(wordListRestriction)
            val hiddenWord = questionWords.random()
            val fourNotLearnedWord = if (questionWords.size < wordListRestriction) {
                val learnedWord = WordFile().dictionary
                    .filter { it.correctAnswersCount >= REPETITIONS_TO_MEMORIZE }
                questionWords + learnedWord.shuffled()
                    .take(wordListRestriction - questionWords.size)
            } else questionWords

            val learningMenu = fourNotLearnedWord.mapIndexed { id, it ->
                "${id + 1} - ${it.translate} \n"
            }.joinToString("") +
                    """
          5 - Пропустить
          6 - Главное меню
                """.trimIndent().cyan()

            println("Выберете перевод слова: ${hiddenWord.text}")
            println(learningMenu)
            when (val response = checkingInput()) {
                in 1..fourNotLearnedWord.size -> checkingCorrectAnswer(
                    hiddenWord.translate,
                    fourNotLearnedWord[response - 1].translate
                )

                fourNotLearnedWord.size + 1 -> continue
                fourNotLearnedWord.size + 2 -> ConsoleMenu().startTheMenu()
                else -> println("Не корректный номер пункта меню".red())
            }
            checkingLearnedWords()
        }
    }

    private fun checkingLearnedWords() {
        if (notLearnedWord.isEmpty()) {
            println("Поздравляем, вы выучили все слова !!!".cyan())
            println("Спасибо, что пользовались нашей программой".cyan())
            exitProcess(0)
        }
    }

    private fun checkingCorrectAnswer(hiddenWord: String, answerWord: String) {
        if (hiddenWord == answerWord) {
            println("Вы правильно перевели слово".cyan())
            val wordFile = WordFile()
            wordFile.dictionary.forEach() {
                if (it.translate == answerWord) {
                    it.correctAnswersCount++
                }
            }
            wordFile.writingToFile(wordFile)
            addNotLearnedWord()
        } else {
            println("Не правильный ответ".red())
        }
    }
}
