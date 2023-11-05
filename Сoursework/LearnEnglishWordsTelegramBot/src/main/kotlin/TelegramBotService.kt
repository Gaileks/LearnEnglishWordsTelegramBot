import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private const val API_TELEGRAM = "https://api.telegram.org/bot"

class TelegramBotService(private val client: HttpClient = HttpClient.newBuilder().build()) {

    fun getUpdates(botToken: String, updateId: Int): String {
        val urlGetUpdates = "$API_TELEGRAM$botToken/getUpdates?offset=$updateId"
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMessage(botToken: String, chatId: Int, message: String): String {
        val sendMessage = "$API_TELEGRAM$botToken/sendMessage?chat_id=$chatId&text=$message"
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(sendMessage)).build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }
}
