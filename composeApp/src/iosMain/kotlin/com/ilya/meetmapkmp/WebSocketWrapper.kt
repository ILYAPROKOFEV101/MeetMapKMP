package com.ilya.meetmapkmp

// iosMain/kotlin/WebSocketWrapper.kt

import com.ilya.meetmapkmp.SocialMap.DataModel.Messages_Chat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect


// Обертка для WebSocketClient для использования в iOS
class WebSocketWrapper(serverUrl: String) {

    private val webSocketClient = WebSocketClient(serverUrl)
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        connect()
    }

    fun connect() {
        webSocketClient.connect()
    }

    fun disconnect() {
        webSocketClient.disconnect()
    }

    fun sendMessage(content: String) {
        val message = Messages_Chat(content = content)
        webSocketClient.sendMessage(message)
    }

    // Функция для наблюдения за сообщениями и отправки их в iOS
    fun observeMessages(callback: (List<Messages_Chat>) -> Unit) {
        scope.launch {
            webSocketClient.messages.collect { messages ->
                callback(messages)
            }
        }
    }
}
