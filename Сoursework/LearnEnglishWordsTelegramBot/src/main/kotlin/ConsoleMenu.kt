import kotlin.system.exitProcess

data class Word(
    val questionWord: String,
    val translate: String,
    var correctAnswersCount: Int,
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
                1 -> startLearningMenu()
                2 -> {
                    val statistics = trainer.getStatistics()
                    println("Выучено ${statistics.learned} из ${statistics.total} слов | ${statistics.percent}%")
                }

                3 -> {
                    trainer.dictionary = trainer.resetProgress()
                }

                0 -> {
                    println("Спасибо, что пользовались нашей программой".cyan())
                    exitProcess(0)
                }

                else -> println("Не корректный номер пункта меню".red())
            }
        }
    }

    private fun startLearningMenu() {
        loop@ while (true) {
            val question = trainer.getNextQuestion()
            if (question != null) {

                println("Выберете перевод слова: ${question.correctAnswer.questionWord}")
                println(question.asConsoleString())

                when (val response = checkingInput()) {
                    in 1..question.variants.size ->
                        if (question.correctAnswer.translate == question.variants[response - 1].translate) {
                            println("Вы правильно перевели слово".cyan())
                            val correctAnswerIndex = trainer.dictionary.indexOf(question.correctAnswer)
                            trainer.dictionary[correctAnswerIndex].correctAnswersCount++
                            trainer.writingToFile(trainer.dictionary)

                        } else println("Не правильный ответ".red() + " - правильный ответ: ${question.correctAnswer.translate}")

                    question.variants.size + 1 -> continue
                    question.variants.size + 2 -> return
                    else -> println("Не корректный номер пункта меню".red())
                }
            } else {
                println("Поздравляем, вы выучили все слова !!!".cyan())
                break@loop
            }
        }
        startTheMenu()
    }

    private fun checkingInput(): Int {
        while (true) {
            readln().toIntOrNull()
                ?.let { return it }
                ?: println("Введенное значение не удовлетворяет требованиям".red())
        }
    }
}