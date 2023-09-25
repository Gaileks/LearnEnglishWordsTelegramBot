package coursework

import kotlin.system.exitProcess

class Learning {

    fun startLearningMenu() {
        val programLogic = ProgramLogic()
        val dataInputOutput = DataInputOutput()
        val consoleMenu = ConsoleMenu()
        while (true) {
            val (hiddenWord, fourNotLearnedWord) = programLogic.listOfResponseOptions()
            val learningMenu = fourNotLearnedWord.mapIndexed { id, it ->
                "${id + 1} - ${it.translate} \n"
            }.joinToString("") +
                    """
          5 - Пропустить
          6 - Главное меню
                """.trimIndent().cyan()

            dataInputOutput.dataOutput("Выберете перевод слова: ${hiddenWord.text}")
            dataInputOutput.dataOutput(learningMenu)
            when (val response = dataInputOutput.checkingInput()) {
                in 1..fourNotLearnedWord.size -> programLogic.checkingCorrectAnswer(
                    hiddenWord.translate,
                    fourNotLearnedWord[response - 1].translate
                )

                fourNotLearnedWord.size + 1 -> continue
                fourNotLearnedWord.size + 2 -> consoleMenu.startTheMenu()
                else -> dataInputOutput.dataOutput("Не корректный номер пункта меню".red())
            }
            programLogic.checkingLearnedWords()
        }
    }
}
