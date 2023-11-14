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

val trainer = LearnWordsTrainer()
var question = trainer.getNextQuestion()

fun main() {
    var lastUpdateId = 0L
    val json = Json { ignoreUnknownKeys = true }
    val trainers = HashMap<Long, LearnWordsTrainer>()
    val telegramBotService = TelegramBotService()


    while (true) {
        Thread.sleep(1000)
        val responseString: String = telegramBotService.getUpdates(lastUpdateId)
        println(responseString)

        val response: Response = json.decodeFromString(responseString)
        if (response.result.isEmpty()) continue
        val sortedUpdates = response.result.sortedBy { it.updateId }
        sortedUpdates.forEach { handleUpdate(it, json, trainers) }
        lastUpdateId = sortedUpdates.last().updateId + 1
    }
}

fun handleUpdate(
    update: Update,
    json: Json,
    trainers: HashMap<Long, LearnWordsTrainer>,
) {

    val telegramBotService = TelegramBotService()
    val message = update.message?.text
    val chatId = update.message?.chat?.id ?: update.callbackQuery?.message?.chat?.id?: return

    val trainer = trainers.getOrPut(chatId){LearnWordsTrainer("$chatId.txt")}

    val data = update.callbackQuery?.data
    println(data)

        when (data?.lowercase()) {
            MAIN_MENU -> {
                telegramBotService.sendMenu(chatId, json)
            }

            RESET_PROGRESS -> {
                trainer.dictionary = trainer.resetProgress()
                telegramBotService.sendMessage(chatId, "Прогресс  обнулен!", json)
                question = trainer.getNextQuestion()
                telegramBotService.sendMenu(chatId, json)
            }

            STATISTICS_CLICKED -> {
                val statistics = trainer.getStatistics()
                telegramBotService.sendMessage(
                    chatId,
                    "Выучено ${statistics.learned} из ${statistics.total} слов | ${statistics.percent}%", json
                )
                telegramBotService.sendMenu(chatId, json)
            }

            LEARN_WORDS_CLICKED -> {
                telegramBotService.sendingQuestionUser(chatId, question, json)
            }
        }
        if (message?.lowercase() == "/start") {
            telegramBotService.sendMenu(chatId, json)

        } else if (question != null && question!!.variants.map { it.questionWord }.contains(data?.lowercase())) {

            if (data?.lowercase() == question!!.correctAnswer.questionWord) {
                telegramBotService.sendMessage(chatId, "Вы правильно перевели слово", json)
                val correctAnswerIndex = trainer.dictionary.indexOf(question!!.correctAnswer)
                trainer.dictionary[correctAnswerIndex].correctAnswersCount++
                trainer.writingToFile(trainer.dictionary)
                question = trainer.getNextQuestion()
                telegramBotService.sendingQuestionUser(chatId, question, json)
            } else {
                telegramBotService.sendMessage(
                    chatId,
                    "Не правильный ответ - правильный ответ: ${question!!.correctAnswer.translate}", json
                )
                question = trainer.getNextQuestion()
                telegramBotService.sendingQuestionUser(chatId, question, json)
            }
        }
    }


