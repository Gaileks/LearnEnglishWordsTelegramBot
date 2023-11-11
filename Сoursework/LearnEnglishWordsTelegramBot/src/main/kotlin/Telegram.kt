fun main() {

    var lastUpdateId = 0
    val telegramBotService = TelegramBotService()
    val trainer = LearnWordsTrainer()
    var question = trainer.getNextQuestion()

    val updateIdRegex: Regex = """"update_id":(\d+)""".toRegex()
    val messageTextRegex: Regex = """"text":"(.+?)"""".toRegex()
    val chatIdRegex: Regex = """"chat":\{"id":(-*\d+)""".toRegex()
    val dataRegex: Regex = """"data":"(.+?)"""".toRegex()

    while (true) {
        Thread.sleep(2000)
        val updates: String = telegramBotService.getUpdates(lastUpdateId)
        println(updates)

        val updateId = updateIdRegex.find(updates)?.groups?.get(1)?.value?.toIntOrNull() ?: continue
        lastUpdateId = updateId + 1
        val message = messageTextRegex.find(updates)?.groups?.get(1)?.value
        val chatId = chatIdRegex.find(updates)?.groups?.get(1)?.value?.toInt()
        val data = dataRegex.find(updates)?.groups?.get(1)?.value
        println(data)

        if (chatId != null) {
            when (data?.lowercase()) {
                MAIN_MENU -> {
                    telegramBotService.sendMenu(chatId)
                }

                RESET_PROGRESS -> {
                    trainer.dictionary = trainer.resetProgress()
                    telegramBotService.sendMessage(chatId, "Прогресс  обнулен!")
                    question = trainer.getNextQuestion()
                    telegramBotService.sendMenu(chatId)
                }

                STATISTICS_CLICKED -> {
                    val statistics = trainer.getStatistics()
                    telegramBotService.sendMessage(
                        chatId,
                        "Выучено ${statistics.learned} из ${statistics.total} слов | ${statistics.percent}%"
                    )
                    telegramBotService.sendMenu(chatId)
                }

                LEARN_WORDS_CLICKED -> {
                    telegramBotService.sendingQuestionUser(chatId, question)
                }
            }
            if (message?.lowercase() == "/start") {
                telegramBotService.sendMenu(chatId)

            } else if (question != null && question.variants.map { it.questionWord }.contains(data?.lowercase())) {

                if (data?.lowercase() == question.correctAnswer.questionWord) {

                    telegramBotService.sendMessage(chatId, "Вы правильно перевели слово")
                    val correctAnswerIndex = trainer.dictionary.indexOf(question.correctAnswer)
                    trainer.dictionary[correctAnswerIndex].correctAnswersCount++
                    trainer.writingToFile(trainer.dictionary)
                    question = trainer.getNextQuestion()
                    telegramBotService.sendingQuestionUser(chatId, question)
                } else {
                    telegramBotService.sendMessage(
                        chatId,
                        "Не правильный ответ - правильный ответ: ${question.correctAnswer.translate}"
                    )
                    question = trainer.getNextQuestion()
                    telegramBotService.sendingQuestionUser(chatId, question)
                }
            }
        }
    }
}
