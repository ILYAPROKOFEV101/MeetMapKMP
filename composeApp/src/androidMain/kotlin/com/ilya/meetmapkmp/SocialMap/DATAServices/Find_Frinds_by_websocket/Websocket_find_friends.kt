import android.util.Log
import com.ilya.MeetingMap.SocialMap.DATAServices.Find_Frinds_by_websocket.WebSocketCallback_frinds
import com.ilya.MeetingMap.SocialMap.DataModel.FindFriends
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONArray
import java.util.concurrent.TimeUnit

class WebSocketFindFriends(
    url: String,
    private val callback: WebSocketCallback_frinds
) : WebSocketListener() {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    private val request = Request.Builder()
        .url(url)
        .build()

    private val webSocket: WebSocket = client.newWebSocket(request, this)

    // Метод для отправки команды по WebSocket
    fun sendCommand(command: String) {
        Log.d("WebSocket_friends", "Отправка команды: $command")
        webSocket.send(command)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("WebSocket_friends", "Соединение установлено")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocket_friends", "Получено сообщение: $text")
        parseFriendList(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("WebSocket_friends", "Получены байты: ${bytes.hex()}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocket_friends", "Закрытие соединения: $code / $reason")
        webSocket.close(1000, null)
    }

    fun stop() {
        Log.d("WebSocket_friends", "Закрытие WebSocket соединения")
        webSocket.close(1000, "Закрытие соединения")
        client.dispatcher.executorService.shutdown()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("WebSocket_friends", "Ошибка соединения: ${t.message}")
    }

    // Метод для парсинга полученного списка друзей
    private fun parseFriendList(json: String) {
        Log.d("WebSocket_friends", "Парсинг списка друзей")
        val jsonArray = JSONArray(json)
        val friendsList = mutableListOf<FindFriends>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val friend = FindFriends(
                key = jsonObject.getString("key"),
                name = jsonObject.getString("name"),
                img = jsonObject.getString("img"),
                friend = jsonObject.getBoolean("friend")
            )
            friendsList.add(friend)
        }
        Log.d("WebSocket_friends", "Получен список друзей: ${friendsList.size} друзей")
        callback.onFriendListReceived(friendsList)
    }
}
