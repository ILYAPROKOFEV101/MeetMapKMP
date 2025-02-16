package com.ilya.meetmapkmp.SocialMap.DATAServices

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


class WebSocketService(
    private var onMessageReceived: ((String) -> Unit)? = null,
    private var onErrorOccurred: ((String) -> Unit)? = null
) {
    private var webSocket: WebSocket? = null
    private var uid: String? = null
    private var key: String? = null

    companion object {
        private const val TAG = "WebSocketService" // Тег для логов
    }

    // Метод для подключения к WebSocket
    fun connect(uid: String, key: String) {
        this.uid = uid
        this.key = key
        Log.d(TAG, "Attempting to connect with uid: $uid, key: $key")
        connectWebSocket()
    }

    private fun connectWebSocket() {
        if (uid == null || key == null) {
            Log.e(TAG, "UID or Key is null. Cannot connect WebSocket.")
            onErrorOccurred?.invoke("UID or Key is null.")
            return
        }

        val url = "wss://meetmap.up.railway.app/get-friends/$uid/$key"
        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient.Builder()
            .pingInterval(10, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.i(TAG, "WebSocket connection opened. Sending initial request for friends...")
                webSocket.send("getFriends")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Message received from server: $text")
                onMessageReceived?.invoke(text) // Вызываем callback для обработки сообщений
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket connection failed: ${t.message}", t)
                onErrorOccurred?.invoke(t.message ?: "Unknown error") // Вызываем callback для обработки ошибок
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.w(TAG, "WebSocket closed with code: $code, reason: $reason")
            }
        })

        Log.d(TAG, "WebSocket connection initiated to URL: $url")
    }

    // Метод для закрытия WebSocket соединения
    fun disconnect() {
        webSocket?.let {
            Log.d(TAG, "Closing WebSocket connection")
            it.close(1000, "Service destroyed")
        } ?: Log.w(TAG, "Attempted to close WebSocket but it was not initialized")
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