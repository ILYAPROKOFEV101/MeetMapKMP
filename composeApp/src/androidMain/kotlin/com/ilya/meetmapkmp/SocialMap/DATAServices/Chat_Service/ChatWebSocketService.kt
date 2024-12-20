package com.ilya.meetmapkmp.SocialMap.DATAServices.Chat_Service






import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ilya.meetmapkmp.SocialMap.DataModel.Messages_Chat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class ChatWebSocketService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val _messages = MutableStateFlow<List<Messages_Chat>>(emptyList())
    val messages: StateFlow<List<Messages_Chat>> = _messages
    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS) // Неограниченное время ожидания сообщений
        .build()
    private var webSocket: WebSocket? = null

    private val TAG = "ChatWebSocketService"

    fun connectToWebSocket(url: String) {
        Log.d(TAG, "Connecting to WebSocket at: $url")
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, WebSocketListenerImpl())
    }

    fun sendMessage(jsonMessage: String) {
        coroutineScope.launch {
            webSocket?.let { socket ->
                val success = socket.send(jsonMessage)
                if (success) {
                    Log.d(TAG, "Сообщение отправлено: $jsonMessage")
                } else {
                    Log.e(TAG, "Не удалось отправить сообщение")
                }
            } ?: Log.e(TAG, "WebSocket не подключён")
        }
    }

    fun disconnect() {
        Log.d(TAG, "Disconnecting from WebSocket")
        webSocket?.close(1000, "Disconnecting")
        webSocket = null
    }

    fun switchConnection(newUrl: String) {
        Log.d(TAG, "Switching connection to: $newUrl")
        disconnect()
        connectToWebSocket(newUrl)
    }

    override fun onDestroy() {
        Log.d(TAG, "Service destroyed")
        disconnect()
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private inner class WebSocketListenerImpl : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "WebSocket connection opened")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(TAG, "Received text message: $text")
            val receivedMessage = parseMessage(text)
            coroutineScope.launch {
                _messages.emit(_messages.value + receivedMessage)
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d(TAG, "Received binary message: ${bytes.hex()}")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "WebSocket is closing: $reason")
            webSocket.close(code, reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "WebSocket closed: $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e(TAG, "WebSocket error: ${t.message}", t)
        }
    }

    private fun parseMessage(json: String): Messages_Chat {
        val jsoname = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
        return jsoname.decodeFromString(Messages_Chat.serializer(), json)
    }
}

