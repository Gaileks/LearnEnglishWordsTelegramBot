package coursework

import kotlin.system.exitProcess

class Learning {
    private val notLearnedWord: MutableList<Word> = mutableListOf()

    init {
        addNotLearnedWord()
        checkingLearnedWords()
    }

    private var fourNotLearnedWord: MutableList<Word> = mutableListOf()

    private fun loadNotLearnedWords(hiddenWord: Word) {
        val wordListRestriction = 3
        if (notLearnedWord.size >= wordListRestriction) {
            fourNotLearnedWord =
                notLearnedWord.filterNot { it == hiddenWord }.shuffled().take(wordListRestriction).toMutableList()

        } else {
            repeat(wordListRestriction) {
                fourNotLearnedWord.add(notLearnedWord[(0..notLearnedWord.lastIndex).random()])
            }
        }
        fourNotLearnedWord.add(hiddenWord)
        fourNotLearnedWord = fourNotLearnedWord.shuffled().toMutableList()
    }

    private fun addNotLearnedWord() {
        WordFile().dictionary.forEach() {
            if (it.correctAnswersCount < REPETITIONS_TO_MEMORIZE) notLearnedWord.add(it)
        }
    }

    fun startLearningMenu() {
        while (true) {
            val hiddenWord = notLearnedWord.random()
            loadNotLearnedWords(hiddenWord)
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
        } else {
            println("Не правильный ответ".red())
        }
    }
}
