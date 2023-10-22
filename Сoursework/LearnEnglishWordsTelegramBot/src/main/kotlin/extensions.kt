
fun String.red() = "\u001b[31m${this}\u001b[0m"
fun String.cyan() = "\u001b[36m${this}\u001b[0m"

fun Question.asConsoleString():String{
   val variants = this.variants
    val learningMenu = (variants.mapIndexed { id, it ->
        "${id + 1} - ${it.translate} \n"
    }.joinToString("")) +
            """
          5 - Пропустить
          6 - Главное меню
                """.trimIndent().cyan()
    return learningMenu
}