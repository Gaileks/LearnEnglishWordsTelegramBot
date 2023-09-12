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
        while (true) {
            println(menuItems)
            when (checkingInput()) {
                1 -> Learning().startLearningMenu()
                2 -> Statistics().getStatisticsInfo()
                3 -> WordFile().resetProgress()
                0 -> {
                    println("Спасио, что пользовались нашей программой".cyan())
                    return
                }

                else -> println("Не корректный номер пункта меню".red())

            }
        }
    }
}

