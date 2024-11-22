import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import okhttp3.WebSocketListener

import okhttp3.*

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.ilya.MeetingMap.Map.Interfaces.WebSocketCallback

import okhttp3.logging.HttpLoggingInterceptor
class WebSocketManager(private val client: OkHttpClient, private val callback: WebSocketCallback?) : WebSocketListener() {

    private var webSocket: WebSocket? = null
    var isConnected: Boolean = false
        private set

    private var disconnectHandler: Handler = Handler(Looper.getMainLooper())
    private var disconnectRunnable: Runnable? = null

    // Настройка WebSocket
    fun setupWebSocket(url: String) {
        Log.d("WebSocket_shake", "url = $url")

        // Закрываем предыдущий WebSocket, если он уже подключен
        if (webSocket != null && isConnected) {
            closeWebSocket()
        }

        try {
            val request: Request = Request.Builder()
                .url(url)
                .build()

            webSocket = client.newWebSocket(request, this)

            // Устанавливаем таймер для автоматического отключения через 20 секунд
            scheduleDisconnect()

        } catch (e: Exception) {
            Log.e("WebSocket_shake", "Error creating WebSocket: ${e.message}")
        }
    }

    // Обработка события открытия сокета
    override fun onOpen(webSocket: WebSocket, response: Response) {
        isConnected = true
        Log.d("WebSocket_shake", "WebSocket opened with response: $response")
    }

    // Обработка события получения сообщения
    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocket_shake", "Raw message received: $text")
        handleReceivedMessage(text)

        // Не закрываем WebSocket сразу, ждем 20 секунд или закрываем вручную
    }

    // Обработка события закрытия сокета
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        isConnected = false
        Log.d("WebSocket_shake", "WebSocket closed with reason: $reason")
        cancelDisconnect()  // Отменяем таймер, так как WebSocket уже закрыт
    }

    // Обработка ошибки подключения
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        isConnected = false
        Log.e("WebSocket_shake", "WebSocket failure: ${t.message}. Response: $response")
        cancelDisconnect()  // Отменяем таймер в случае ошибки
    }

    // Обработка полученного сообщения
    private fun handleReceivedMessage(message: String) {
        try {
            val gson = Gson()
            val receivedDataList: List<ReceivedData> = try {
                gson.fromJson(message, Array<ReceivedData>::class.java).toList()
            } catch (e: JsonSyntaxException) {
                listOf(gson.fromJson(message, ReceivedData::class.java))
            }

            Log.d("WebSocket_shake", "Received data count: ${receivedDataList.size}")
            Log.d("WebSocket_shake", "Received data: $receivedDataList")

            // Возвращаем данные через callback
            callback?.onMessageReceived(receivedDataList)
        } catch (e: Exception) {
            Log.e("WebSocket_shake", "Error parsing JSON: ${e.message}")
        }
    }

    // Функция закрытия WebSocket
    fun closeWebSocket() {
        webSocket?.close(1000, "Closed by user or timeout")
        webSocket = null
        isConnected = false
        cancelDisconnect()  // Отменяем таймер
    }

    // Запуск таймера для автоматического отключения через 20 секунд
    private fun scheduleDisconnect() {
        cancelDisconnect()  // Отменяем старый таймер, если он был запущен

        disconnectRunnable = Runnable {
            Log.d("WebSocket_shake", "Closing WebSocket after 20 seconds")
            closeWebSocket()  // Автоматическое отключение WebSocket
        }
        disconnectHandler.postDelayed(disconnectRunnable!!, 20 * 1000)  // Запуск таймера на 20 секунд
    }

    // Отмена таймера отключения
    private fun cancelDisconnect() {
        disconnectRunnable?.let {
            disconnectHandler.removeCallbacks(it)
        }
        disconnectRunnable = null
    }

    // Модель данных для полученного JSON
    data class ReceivedData(
        val user_name: String,
        val img: String,
        val key: String
    )
}

