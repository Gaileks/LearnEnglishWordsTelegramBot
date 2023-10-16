package coursework

import kotlin.system.exitProcess

class DataInputOutput {

    fun checkingInput(): Int {
        while (true) {
            dataEntry().toIntOrNull()
                ?.let { return it }
                ?: dataOutput("Введенное значение не удовлетворяет требованиям".red())
        }
    }

    fun endProgram(): Nothing = exitProcess(0)

    fun dataOutput(message: Any?) {
        println(message)
    }

    private fun dataEntry() = readln()

}