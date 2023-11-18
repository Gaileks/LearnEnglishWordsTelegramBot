import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SendMessageRequest(
    @SerialName("chat_id")
    val chatId: Long,
    @SerialName("text")
    val text: String,
    @SerialName("reply_markup")
    val replyMarkup: ReplyMarkup? = null,
)

@Serializable
data class ReplyMarkup(
    @SerialName("inline_keyboard")
    val inlineKeyboard: List<List<InlineKeyboard>>,
)

@Serializable
data class InlineKeyboard(
    @SerialName("text")
    val text: String,
    @SerialName("callback_data")
    val callbackData: String,


    )

@Serializable
data class Update(
    @SerialName("update_id")
    val updateId: Long,
    @SerialName("message")
    val message: Message? = null,
    @SerialName("callback_query")
    val callbackQuery: CallbackQuery? = null,
)

@Serializable
data class Response(
    @SerialName("result")
    val result: List<Update>,
)

@Serializable
data class Chat(
    @SerialName("id")
    val id: Long
)

@Serializable
data class Message(
    @SerialName("text")
    val text: String,
    @SerialName("chat")
    val chat: Chat,

    )

@Serializable
data class CallbackQuery(
    @SerialName("data")
    val data: String,
    @SerialName("message")
    val message: Message? = null,
)

val waitingTime = Thread.sleep(2000)
val trainer = LearnWordsTrainer()
var question = trainer.getNextQuestion()

fun main(args: Array<String>) {
    val botToken = args[0]
    var lastUpdateId = 0L
    val json = Json { ignoreUnknownKeys = true }
    val trainers = HashMap<Long, LearnWordsTrainer>()
    val telegramBotService = TelegramBotService()

    while (true) {
        waitingTime
        val responseString: String = runCatching {
            telegramBotService.getUpdates(lastUpdateId, botToken)
        }
            .getOrElse {
                println("${it.message}")
                ""
            }
        if (responseString == "") continue

        val response: Response = json.decodeFromString(responseString)
        if (response.result.isEmpty()) continue
        val sortedUpdates = response.result.sortedBy { it.updateId }
        sortedUpdates.forEach { handleUpdate(it, trainers, botToken) }
        lastUpdateId = sortedUpdates.last().updateId + 1
    }
}

fun handleUpdate(
    update: Update,
    trainers: HashMap<Long, LearnWordsTrainer>,
    botToken: String,
) {
    val telegramBotService = TelegramBotService()
    val message = update.message?.text
    val chatId = update.message?.chat?.id ?: update.callbackQuery?.message?.chat?.id ?: return
    val trainer = trainers.getOrPut(chatId) { LearnWordsTrainer("$chatId.txt") }
    val data = update.callbackQuery?.data
    println(data)

    when (data?.lowercase()) {
        MAIN_MENU -> {
            telegramBotService.sendMenu(chatId, botToken)
        }

        RESET_PROGRESS -> {
            trainer.dictionary = trainer.resetProgress()
            telegramBotService.sendMessage(chatId, "Прогресс  обнулен!", botToken)
            question = trainer.getNextQuestion()
            telegramBotService.sendMenu(chatId, botToken)
        }

        STATISTICS_CLICKED -> {
            val statistics = trainer.getStatistics()
            telegramBotService.sendMessage(
                chatId,
                "Выучено ${statistics.learned} из ${statistics.total} слов | ${statistics.percent}%", botToken
            )
            telegramBotService.sendMenu(chatId, botToken)
        }

        LEARN_WORDS_CLICKED -> {
            telegramBotService.sendingQuestionUser(chatId, question, botToken)
        }
    }
    if (message?.lowercase() == "/start") {
        telegramBotService.sendMenu(chatId, botToken)

    } else if (question?.variants?.map { it.questionWord }?.contains(data?.lowercase()) == true) {

        if (data?.lowercase() == question?.correctAnswer?.questionWord) {
            telegramBotService.sendMessage(chatId, "Вы правильно перевели слово", botToken)
            val correctAnswerIndex = trainer.dictionary.indexOf(question?.correctAnswer)
            trainer.dictionary[correctAnswerIndex].correctAnswersCount++
            trainer.writingToFile(trainer.dictionary)
            question = trainer.getNextQuestion()
            telegramBotService.sendingQuestionUser(chatId, question, botToken)
        } else {
            telegramBotService.sendMessage(
                chatId,
                "Не правильный ответ - правильный ответ: ${question?.correctAnswer?.translate}", botToken
            )
            question = trainer.getNextQuestion()
            telegramBotService.sendingQuestionUser(chatId, question, botToken)
        }
    }
}