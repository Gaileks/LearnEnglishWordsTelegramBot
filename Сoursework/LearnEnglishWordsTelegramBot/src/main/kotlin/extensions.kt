package coursework

const val REPETITIONS_TO_MEMORIZE: Int = 3

fun String.red() = "\u001b[31m${this}\u001b[0m"
fun String.cyan() = "\u001b[36m${this}\u001b[0m"

fun checkingInput(): Int {
    while (true) {
        readln().toIntOrNull()
            ?.let { return it }
            ?: println("Введенное значение не удовлетворяет требованиям".red())
    }
}