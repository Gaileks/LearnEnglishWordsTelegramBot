import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private const val API_TELEGRAM = "https://api.telegram.org/bot"
const val STATISTICS_CLICKED = "statistics_clicked"
const val LEARN_WORDS_CLICKED = "learn_words_clicked"
const val RESET_PROGRESS = "reset_progress"
const val MAIN_MENU = "/start"

class TelegramBotService(
    private val client: HttpClient = HttpClient.newBuilder().build(),
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun getUpdates(updateId: Long, botToken: String): Response {
        val urlGetUpdates = "$API_TELEGRAM$botToken/getUpdates?offset=$updateId"
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return json.decodeFromString(response.body())
    }

    fun sendMessage(chatId: Long, message: String, botToken: String): String {
        val sendMessage = "$API_TELEGRAM$botToken/sendMessage"
        val requestBody = SendMessageRequest(
            chatId,
            message,
        )
        val requestBodyString = json.encodeToString(requestBody)
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(sendMessage))
            .header("Content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBodyString))
            .build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMenu(chatId: Long, botToken: String): String {
        val sendMessage = "$API_TELEGRAM$botToken/sendMessage"
        val requestBody = SendMessageRequest(
            chatId,
            "Основное меню",
            ReplyMarkup(
                listOf(
                    listOf(
                        InlineKeyboard("Изучить слова", LEARN_WORDS_CLICKED),
                    ),
                    listOf(
                        InlineKeyboard("Статистика", STATISTICS_CLICKED),
                        InlineKeyboard("Сброс прогресса", RESET_PROGRESS),
                    ),
                )
            )
        )

        val requestBodyString = json.encodeToString(requestBody)

        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(sendMessage))
            .header("Content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBodyString))
            .build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendingQuestionUser(chatId: Long, question: Question?, botToken: String): String {
        if (question != null) {
            val sendMessage = "$API_TELEGRAM$botToken/sendMessage"

            val requestBody = SendMessageRequest(
                chatId,
                "Выберите перевод слова: ${question.correctAnswer.questionWord}",
                ReplyMarkup
                    (
                    listOf(
                        question.variants.filterIndexed { index, _ -> index >= question.variants.size / 2 }
                            .map { word ->
                                InlineKeyboard(
                                    word.translate, word.questionWord
                                )
                            },
                        question.variants.filterIndexed { index, _ -> index < question.variants.size / 2 }.map { word ->
                            InlineKeyboard(
                                word.translate, word.questionWord
                            )
                        },
                        listOf(
                            InlineKeyboard("Вернуться в основное меню", MAIN_MENU)
                        )
                    )
                )
            )
            val requestBodyString = json.encodeToString(requestBody)

            val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(sendMessage))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyString))
                .build()
            val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())

            return response.body()
        } else
            return """
                ${sendMessage(chatId, "Поздравляем, вы выучили все слова !!!", botToken)}
                ${sendMenu(chatId, botToken)}
            """.trimIndent()
    }
}
