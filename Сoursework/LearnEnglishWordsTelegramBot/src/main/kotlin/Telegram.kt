import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val trainers = HashMap<Long, LearnWordsTrainer>()
    val telegramBotService = TelegramBotService(botToken = botToken)

    while (true) {
        waitingTime
        val response: Response = runCatching {
            telegramBotService.getUpdates(lastUpdateId)
        }
            .getOrElse {
                println("${it.message}")
                Response(result = listOf())
            }

        if (response.result.isEmpty()) continue
        val sortedUpdates = response.result.sortedBy { it.updateId }
        sortedUpdates.forEach { handleUpdate(it, trainers,telegramBotService) }
        lastUpdateId = sortedUpdates.last().updateId + 1
    }
}

fun handleUpdate(
    update: Update,
    trainers: HashMap<Long, LearnWordsTrainer>,

    telegramBotService: TelegramBotService,
) {
    val message = update.message?.text
    val chatId = update.message?.chat?.id ?: update.callbackQuery?.message?.chat?.id ?: return
    val trainer = trainers.getOrPut(chatId) { LearnWordsTrainer("$chatId.txt") }
    val data = update.callbackQuery?.data
    println(data)

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

    } else if (question?.variants?.map { it.questionWord }?.contains(data?.lowercase()) == true) {

        if (data?.lowercase() == question?.correctAnswer?.questionWord) {
            telegramBotService.sendMessage(chatId, "Вы правильно перевели слово")
            val correctAnswerIndex = trainer.dictionary.indexOf(question?.correctAnswer)
            trainer.dictionary[correctAnswerIndex].correctAnswersCount++
            trainer.writingToFile(trainer.dictionary)
            question = trainer.getNextQuestion()
            telegramBotService.sendingQuestionUser(chatId, question)
        } else {
            telegramBotService.sendMessage(
                chatId,
                "Не правильный ответ - правильный ответ: ${question?.correctAnswer?.translate}"
            )
            question = trainer.getNextQuestion()
            telegramBotService.sendingQuestionUser(chatId, question)
        }
    }
}