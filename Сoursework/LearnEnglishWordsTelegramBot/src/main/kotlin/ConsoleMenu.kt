package coursework

class ConsoleMenu {

    fun startTheMenu() {
        val menuItems = """
    (Введите пункт меню) 
    Меню: 
     1 – Учить слова
     2 – Статистика
     0 – Выход    
""".trimIndent()

        while (true) {
            println(menuItems)
            when (checkingInput()) {
                1 -> Learning().startLearningMenu()
                2 -> Statistics().getStatisticsInfo()
                0 -> {
                    println("Спасио, что пользовались нашей программой".cyan())
                    return
                }

                else -> println("Не корректный номер пункта меню".red())

            }
        }
    }

    private fun checkingInput(): Int {
        var number: Int?
        do {
            number = readln().toIntOrNull()
            if (number == null) println("Введенное значение не удовлетворяет требованиям".red())
        } while (number == null)
        return number
    }
}

