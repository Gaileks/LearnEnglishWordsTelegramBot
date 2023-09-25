package coursework

class ConsoleMenu {

    private val menuItems: String = """
    (Введите пункт меню) 
    Меню: 
     1 – Учить слова
     2 – Статистика
     3 - Обнулить прогресс 
     0 – Выход    
""".trimIndent()

    fun startTheMenu() {
        val dataInputOutput = DataInputOutput()
        val learning = Learning()
        val programLogic = ProgramLogic()

        while (true) {
            dataInputOutput.dataOutput(menuItems)
            when (dataInputOutput.checkingInput()) {
                1 -> {
                    programLogic.checkingLearnedWords()
                    learning.startLearningMenu()
                }

                2 -> Statistics().getStatisticsInfo()
                3 -> programLogic.resetProgress()
                0 -> {
                    dataInputOutput.dataOutput("Спасибо, что пользовались нашей программой".cyan())
                    dataInputOutput.endProgram()
                }

                else -> dataInputOutput.dataOutput("Не корректный номер пункта меню".red())

            }
        }
    }
}

