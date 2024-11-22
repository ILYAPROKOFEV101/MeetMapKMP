package com.ilya.MeetingMap.Map.Server_API.WebSocketClient

import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class  WebSocketClient(private val url: String) : WebSocketListener() {
    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket
    private var deferredResponse: CompletableDeferred<String>? = null

    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("WebSocket", "Connected to the server")
            onConnectionOpened()
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocket", "Received message: $text")
            handleResponse(text)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket", "Closing: $code / $reason")
            onConnectionClosing(code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("WebSocket", "Error: ${t.message}", t)
            deferredResponse?.completeExceptionally(t)
            onConnectionFailure(t, response)
            retryConnection()
        }
    }

    fun start() {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        if (::webSocket.isInitialized) {
            webSocket.send(message)
            Log.d("WebSocket", "Sent: $message")
        } else {
            Log.e("WebSocket", "WebSocket is not initialized!")
        }
    }

    fun close() {
        if (::webSocket.isInitialized) {
            webSocket.close(1000, "Goodbye!")
        } else {
            Log.e("WebSocket", "WebSocket is not initialized!")
        }
    }

    open fun onConnectionOpened() {}
    open fun onConnectionClosing(code: Int, reason: String) {}
    open fun onConnectionFailure(t: Throwable, response: Response?) {}

    fun sendCommandAndGetResponse(command: String): Deferred<String> {
        deferredResponse = CompletableDeferred()
        sendMessage(command)
        return deferredResponse!!
    }

    private fun handleResponse(text: String) {
        Log.d("WebSocket", "Received message: $text")
        deferredResponse?.let {
            it.complete(text)
            deferredResponse = null
        }
    }

    private fun retryConnection() {
        Log.d("WebSocket", "Retrying connection in 2 seconds...")
        Handler(Looper.getMainLooper()).postDelayed({
            start()
        }, 2000)
    }
}