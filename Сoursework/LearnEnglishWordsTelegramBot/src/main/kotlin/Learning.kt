package coursework

import kotlin.system.exitProcess

class Learning {
    private val notLearnedWord: MutableList<Word> = mutableListOf()

    init {
        addNotLearnedWord()
        checkingLearnedWords()
    }

    private var fourNotLearnedWord: MutableList<Word> = mutableListOf()

    private fun getFourNotLearnedWord() {
        if (notLearnedWord.size >= 4) {
            fourNotLearnedWord = notLearnedWord.shuffled().take(4).toMutableList()
        } else {
            repeat(4) {
                fourNotLearnedWord.add(notLearnedWord[(0..notLearnedWord.lastIndex).random()])
            }
        }
    }

    private fun addNotLearnedWord() {
        WordFile().dictionary.forEach() {
            if (it.correctAnswersCount < 3) notLearnedWord.add(it)
        }
    }

    fun startLearningMenu() {

        while (true) {
            getFourNotLearnedWord()
            val hiddenWord = notLearnedWord.shuffled()[0]
            val learningMenu = """          
          1 - ${fourNotLearnedWord[0].translate}
          2 - ${fourNotLearnedWord[1].translate}
          3 - ${fourNotLearnedWord[2].translate}
          4 - ${fourNotLearnedWord[3].translate}       
               """.trimIndent() +
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
            //TODO Изменить количество правильных ответов (correctAnswersCount)
        } else {
            println("Не правильный ответ".red())
        }
    }
}
