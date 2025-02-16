import android.util.Log

import com.ilya.MeetingMap.SocialMap.DataModel.FindFriends
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONArray
import java.util.concurrent.TimeUnit




class Websocket_find_friends(
    private var onMessageReceived: ((String) -> Unit)? = null,
    private var onErrorOccurred: ((String) -> Unit)? = null
) {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    // Метод для подключения к WebSocket
    fun connect(uid: String, key: String) {
        val url = "wss://meetmap.up.railway.app/findFriends/$uid/$key"
        val request = Request.Builder().url(url).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket_friends", "Соединение установлено")
                webSocket.send("getFriends") // Отправляем команду для получения списка друзей
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket_friends", "Получено сообщение: $text")
                onMessageReceived?.invoke(text) // Вызываем функцию обратного вызова для обработки сообщений
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket_friends", "Закрытие соединения: $code / $reason")
                webSocket.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket_friends", "Ошибка соединения: ${t.message}")
                onErrorOccurred?.invoke(t.message ?: "Unknown error") // Вызываем функцию обратного вызова для обработки ошибок
            }
        })
    }

    // Метод для закрытия WebSocket соединения
    fun disconnect() {
        webSocket?.let {
            Log.d("WebSocket_friends", "Закрытие WebSocket соединения")
            it.close(1000, "Закрытие соединения")
        }
        client.dispatcher.executorService.shutdown()
    }

    // Метод для отправки команды по WebSocket
    fun sendCommand(command: String) {
        webSocket?.send(command)
    }

    // Методы для установки callback-функций
    fun setOnMessageReceivedListener(listener: (String) -> Unit) {
        this.onMessageReceived = listener
    }

    fun setOnErrorOccurredListener(listener: (String) -> Unit) {
        this.onErrorOccurred = listener
    }
}