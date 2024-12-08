package com.ilya.ServerRESTAPI

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class WebSocketClient(private val url: String) {
    private val client = HttpClient {
        install(WebSockets)
    }

    private var session: WebSocketSession? = null

    private val _incomingMessages = MutableSharedFlow<String>()
    val incomingMessages: SharedFlow<String> get() = _incomingMessages

    /**
     * Подключение к WebSocket.
     */
    suspend fun connect() {
        try {
            session = client.webSocketSession { url }
            listenIncomingMessages()
        } catch (e: Exception) {
            println("WebSocket connection failed: ${e.message}")
        }
    }

    /**
     * Прослушивание входящих сообщений.
     */
    private suspend fun listenIncomingMessages() {
        session?.let { session ->
            try {
                for (frame in session.incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            _incomingMessages.emit(frame.readText())
                        }
                        else -> Unit
                    }
                }
            } catch (e: Exception) {
                println("Error while receiving: ${e.message}")
            }
        }
    }

    /**
     * Отправка сообщения в WebSocket.
     */
    suspend fun sendMessage(message: String) {
        try {
            session?.send(Frame.Text(message))
        } catch (e: Exception) {
            println("Error sending message: ${e.message}")
        }
    }

    /**
     * Отключение от WebSocket.
     */
    suspend fun disconnect() {
        try {
            session?.close()
        } catch (e: Exception) {
            println("Error closing session: ${e.message}")
        }
    }
}
