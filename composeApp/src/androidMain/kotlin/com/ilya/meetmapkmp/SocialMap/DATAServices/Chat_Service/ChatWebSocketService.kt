package com.ilya.meetmapkmp.SocialMap.DATAServices.Chat_Service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ilya.meetmapkmp.SocialMap.DataModel.Messageformat
import com.ilya.meetmapkmp.SocialMap.DataModel.Messages_Chat

import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.json.Json



class ChatWebSocketService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var webSocketSession: DefaultClientWebSocketSession? = null
    private val _messages = MutableStateFlow<List<Messages_Chat>>(emptyList())
    val messages: StateFlow<List<Messages_Chat>> = _messages
    private val httpClient = HttpClient(CIO) {
        install(WebSockets)
    }
    // Определяем session как глобальную переменную
    var open_session: WebSocketSession? = null
        private set // Ограничиваем доступ на изменение снаружи

    // Логирование для отладки
    private val TAG = "ChatWebSocketService"

    // Метод для переключения соединения на новый URL
    fun switchConnection(newUrl: String) {
        Log.d(TAG, "Switching connection to: $newUrl")
        coroutineScope.launch {
            // Закрываем текущее соединение, если оно существует
            webSocketSession?.close(CloseReason(CloseReason.Codes.NORMAL, "Switching to new URL"))
            webSocketSession = null

            try {
                // Подключаемся к новому URL
                webSocketSession = httpClient.webSocketSession(urlString = newUrl).also { session ->
                    Log.d(TAG, "Successfully connected to $newUrl")
                    receiveMessages(session)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error switching connection: ${e.message}", e)
            }
        }
    }
// hello world
    suspend fun receiveMessages(session: WebSocketSession) {
        try {
            for (message in session.incoming) {
                if (message is Frame.Text) {
                    val json = message.readText()
                    val receivedMessage = parseMessage(json)
                    Log.d(TAG, "Received message: $receivedMessage")
                    _messages.emit(_messages.value + receivedMessage)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error receiving messages: ${e.message}", e)
        }
    }

    private val jsoname = Json {
        ignoreUnknownKeys = true // Игнорируем неизвестные ключи
    }     // Метод для парсинга JSON в объект Messages

    private fun parseMessage(json: String): Messages_Chat {
        return jsoname.decodeFromString<Messages_Chat>(json) // Используем настроенный Json
    }

    // Метод для подключения к WebSocket
    fun connectToWebSocket(url: String) {
        Log.d(TAG, "Connecting to WebSocket at: $url")
        coroutineScope.launch {
            try {
                webSocketSession = httpClient.webSocketSession(urlString = url).also { session ->
                    Log.d(TAG, "Successfully connected to WebSocket at $url")
                    open_session = session
                    receiveMessages(session) // Получение сообщений при подключении
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error connecting to WebSocket: ${e.message}", e) // Обработка ошибок подключения
            }
        }
    }

    fun sendMessage(jsonMessage: String) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                if (open_session!!.isActive) {
                    open_session!!.send(Frame.Text(jsonMessage))
                    Log.d(TAG, "Сообщение отправлено: $jsonMessage")
                } else {
                    Log.e(TAG, "Соединение не активно, переподключение...")
                }
            } catch (e: Exception){
                Log.d(TAG, "Error sending message: ${e.message}")
            }
        }
    }

    fun disconnect() {
        Log.d(TAG, "Disconnecting from WebSocket")
        coroutineScope.launch {
            webSocketSession?.close(CloseReason(CloseReason.Codes.NORMAL, "Disconnecting"))
            webSocketSession = null
            Log.d(TAG, "Disconnected from WebSocket")
        }
    }

    fun broadcastMessage(message: String) {
        val intent = Intent("com.ilya.MeetingMap.NEW_MESSAGE")
        intent.putExtra("message_content", message)
        applicationContext.sendBroadcast(intent) // Применение applicationContext вместо context
        Log.d(TAG, "Broadcasting message: $message")
    }

    override fun onDestroy() {
        Log.d(TAG, "Service destroyed")
        disconnect() // Используйте disconnect для закрытия соединения
        coroutineScope.cancel() // Отменяем корутины
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
