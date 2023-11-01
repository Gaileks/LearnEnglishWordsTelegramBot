fun main(args: Array<String>) {

    val botToken = args[0]
    var lastUpdateId = 0
    val telegramBotService = TelegramBotService()

    while (true) {
        Thread.sleep(2000)
        val updates: String = telegramBotService.getUpdates(botToken, lastUpdateId)
        println(updates)
        val updateIdRegex: Regex = """"update_id":(\d+)""".toRegex()
        val messageTextRegex: Regex = """"text":"(.+?)"""".toRegex()
        val chatIdRegex: Regex = """"chat":\{"id":(\d+)""".toRegex()
        val updateId = updateIdRegex.find(updates)?.groups?.get(1)?.value?.toIntOrNull() ?: continue
        lastUpdateId = updateId + 1
        val message = messageTextRegex.find(updates)?.groups?.get(1)?.value
        val chatId = chatIdRegex.find(updates)?.groups?.get(1)?.value?.toInt()

        if (message?.lowercase() == "hello" && chatId != null) {
            telegramBotService.sendMessage(botToken, chatId, "Hello")
        }
    }
}
