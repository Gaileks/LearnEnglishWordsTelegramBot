import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets

private const val API_TELEGRAM = "https://api.telegram.org/bot"
const val STATISTICS_CLICKED = "statistics_clicked"
const val LEARN_WORDS_CLICKED = "learn_words_clicked"
const val RESET_PROGRESS = "reset_progress"
const val MAIN_MENU = "/start"

class TelegramBotService(private val client: HttpClient = HttpClient.newBuilder().build()) {

    fun getUpdates(botToken: String, updateId: Int): String {
        val urlGetUpdates = "$API_TELEGRAM$botToken/getUpdates?offset=$updateId"
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMessage(botToken: String, chatId: Int, message: String): String {
        val encoded = URLEncoder.encode(
            message,
            StandardCharsets.UTF_8
        )
        val sendMessage = "$API_TELEGRAM$botToken/sendMessage?chat_id=$chatId&text=$encoded"
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(sendMessage)).build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMenu(botToken: String, chatId: Int): String {
        val sendMessage = "$API_TELEGRAM$botToken/sendMessage"
        val sendMenuBody = """
            {
                "chat_id": $chatId,
                "text": "Основное меню",
                "reply_markup": {
                    "inline_keyboard": [
                        [
                            {
                                "text": "Изучить слова",
                                "callback_data": "$LEARN_WORDS_CLICKED"
                            },
                            {
                                "text": "Статистика",
                                "callback_data": "$STATISTICS_CLICKED"
                            },
                            {
                                "text": "Сброс прогресса",
                                "callback_data": "$RESET_PROGRESS"
                            }
                        ]
                    ]
                } 
            }        
            
        """.trimIndent()

        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(sendMessage))
            .header("Content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendMenuBody))
            .build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendingQuestionUser(botToken: String, chatId: Int, question: Question?): String {
        if (question != null) {
            val sendMessage = "$API_TELEGRAM$botToken/sendMessage"

            val sendMenuBody = """
            {
                "chat_id": $chatId,                
                "text": "Выберите перевод слова: ${question.correctAnswer.questionWord}",
                "reply_markup": {
                    "inline_keyboard": [
                        [
                            {
                                "text": "${question.variants[0].translate}",
                                "callback_data": "${question.variants[0].questionWord}"
                            },
                            {
                                "text": "${question.variants[1].translate}",
                                "callback_data": "${question.variants[1].questionWord}"
                            },
                                {
                                "text": "${question.variants[2].translate}",
                                "callback_data": "${question.variants[2].questionWord}"
                            },
                                {
                                "text": "${question.variants[3].translate}",
                                "callback_data": "${question.variants[3].questionWord}"
                            }                            
                        ],
                         [                          
                            {
                                "text": "Вернуться в основное меню",
                                "callback_data": "$MAIN_MENU"
                            }
                        ]
                    ]
                } 
            }        
            
        """.trimIndent()

            val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(sendMessage))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(sendMenuBody))
                .build()
            val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())

            return response.body()
        } else
            return """
                ${sendMessage(botToken, chatId, "Поздравляем, вы выучили все слова !!!")}
                ${sendMenu(botToken, chatId)}
            """.trimIndent()
    }
}
