package com.ilya.MeetingMap.SocialMap.DATAServices

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

interface WebSocketListenerCallback {
    fun onMessageReceived(message: String)
    fun onErrorOccurred(error: String)
}

class WebSocketService(private val callback: WebSocketListenerCallback, private val context: Context) {

    private var webSocket: WebSocket? = null
    private var uid: String? = null
    private var key: String? = null



    companion object {
        private const val TAG = "WebSocketService" // Тег для логов
    }




    fun connect(uid: String, key: String) {
        this.uid = uid
        this.key = key
        Log.d(TAG, "Attempting to connect with uid: $uid, key: $key")
        connectWebSocket()
    }



    private fun connectWebSocket() {
        if (uid == null || key == null) {
            Log.e(TAG, "UID or Key is null. Cannot connect WebSocket.")
            callback.onErrorOccurred("UID or Key is null.")
            return
        }

            val url = "wss://meetmap.up.railway.app/get-friends/$uid/$key"
        //val url = "wss://meetmap.up.railway.app/get-friends/NL85HoOb7FVYP8oDsPu1z9oml1o2/6GkAx0f6cJcWCmihMTpTe41IsqFMIV"
        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient.Builder()
            .pingInterval(10, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.i(TAG, "WebSocket connection opened. Sending initial request for friends...")
                webSocket.send("getFriends")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Message received from server: $text")
                callback.onMessageReceived(text) // Передача сообщения через интерфейс

            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket connection failed: ${t.message}", t)
                callback.onErrorOccurred(t.message ?: "Unknown error") // Обработка ошибок через интерфейс
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.w(TAG, "WebSocket closed with code: $code, reason: $reason")
            }
        }

        try {
            webSocket = client.newWebSocket(request, webSocketListener)
            Log.d(TAG, "WebSocket connection initiated to URL: $url")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initiate WebSocket connection: ${e.message}", e)
            callback.onErrorOccurred("Failed to connect: ${e.message}")
        }
    }

    fun closeWebSocket() {
        webSocket?.let {
            Log.d(TAG, "Closing WebSocket connection")
            it.close(1000, "Service destroyed")
        } ?: Log.w(TAG, "Attempted to close WebSocket but it was not initialized")
    }



}