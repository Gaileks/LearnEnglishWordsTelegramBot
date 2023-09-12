package coursework

import kotlin.system.exitProcess

class Learning {
    private val notLearnedWord: MutableList<Word> = mutableListOf()

    init {
        addNotLearnedWord()
        checkingLearnedWords()
    }

    private var fourNotLearnedWord: MutableList<Word> = mutableListOf()

    private fun loadNotLearnedWords() {
        val wordListRestriction = 4
        if (notLearnedWord.size >= wordListRestriction) {
            fourNotLearnedWord = notLearnedWord.shuffled().take(wordListRestriction).toMutableList()
        } else {
            repeat(wordListRestriction) {
                fourNotLearnedWord.add(notLearnedWord[(0..notLearnedWord.lastIndex).random()])
            }
        }
    }

    private fun addNotLearnedWord() {

        WordFile().dictionary.forEach() {
            if (it.correctAnswersCount < REPETITIONS_TO_MEMORIZE) notLearnedWord.add(it)
        }
    }

    fun startLearningMenu() {

        while (true) {
            loadNotLearnedWords()
            val hiddenWord = notLearnedWord.shuffled()[0]
            val learningMenu = fourNotLearnedWord.mapIndexed { id, it ->
                "${id + 1} - ${it.translate} \n"
            }.joinToString("") +
                    """
          5 - Пропустить
          6 - Главное меню
                """.trimIndent().cyan()

            println("Выберете перевод слова: ${hiddenWord.text}")
            println(learningMenu)
            when (checkingInput()) {
                1 -> checkingCorrectAnswer(hiddenWord.translate, fourNotLearnedWord[0].translate)
                2 -> checkingCorrectAnswer(hiddenWord.translate, fourNotLearnedWord[1].translate)
                3 -> checkingCorrectAnswer(hiddenWord.translate, fourNotLearnedWord[2].translate)
                4 -> checkingCorrectAnswer(hiddenWord.translate, fourNotLearnedWord[3].translate)
                5 -> continue
                6 -> ConsoleMenu().startTheMenu()
                else -> println("Не корректный номер пункта меню".red())
            }
        }
    }

    private fun checkingLearnedWords() {
        if (notLearnedWord.isEmpty()) {
            println("Поздравляем, вы выучили все слова !!!".cyan())
            println("Спасио, что пользовались нашей программой".cyan())
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
        } else {
            println("Не правильный ответ".red())
        }
    }
}
