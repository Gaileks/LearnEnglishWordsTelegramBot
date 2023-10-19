data class Word(
    val questionWord: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

class ConsoleMenu {

    private val trainer = LearnWordsTrainer()

    private val menuItems: String = """
    (Введите пункт меню) 
    Меню: 
     1 – Учить слова
     2 – Статистика
     3 - Обнулить прогресс 
     0 – Выход    
""".trimIndent()

    fun startTheMenu() {
        while (true) {

            println(menuItems)
            when (checkingInput()) {
                1 -> {
                    checkingLearnedWords()
                    startLearningMenu()
                }

                2 -> {
                    val statistics = trainer.getStatistics()
                    println("Выучено ${statistics.learned} из ${statistics.total} слов | ${statistics.percent}%")
                }

                3 -> trainer.resetProgress()
                0 -> {
                    println("Спасибо, что пользовались нашей программой".cyan())
                    trainer.endProgram()
                }

                else -> println("Не корректный номер пункта меню".red())
            }
        }
    }

    private fun startLearningMenu() {
        val consoleMenu = ConsoleMenu()
        while (true) {
            val question = trainer.getNextQuestion()
            if (question != null) {

                println("Выберете перевод слова: ${question.correctAnswer.questionWord}")
                println(question.asConsoleString())

                when (val response = checkingInput()) {
                    in 1..question.variants.size -> if (trainer.checkingCorrectAnswer(
                            question.correctAnswer.translate,
                            question.variants[response - 1].translate
                        )
                    ) {
                        println("Вы правильно перевели слово".cyan())
                        checkingLearnedWords()
                    } else println("Не правильный ответ".red() + " - правильный ответ: ${question.correctAnswer.translate}")

                    question.variants.size + 1 -> continue
                    question.variants.size + 2 -> consoleMenu.startTheMenu()
                    else -> println("Не корректный номер пункта меню".red())
                }
            }
        }
    }

    private fun checkingLearnedWords() {
        val question = trainer.getNextQuestion()
        if (question == null) {
            println("Поздравляем, вы выучили все слова !!!".cyan())
            startTheMenu()
        }
    }

    private fun checkingInput(): Int {
        while (true) {
            readln().toIntOrNull()
                ?.let { return it }
                ?: println("Введенное значение не удовлетворяет требованиям".red())
        }
    }
}



