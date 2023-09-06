package coursework

fun String.red() = "\u001b[31m${this}\u001b[0m"
fun String.cyan() = "\u001b[36m${this}\u001b[0m"

fun checkingInput(): Int {
    var number: Int?
    do {
        number = readln().toIntOrNull()
        if (number == null) println("Введенное значение не удовлетворяет требованиям".red())
    } while (number == null)
    return number
}