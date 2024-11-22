package com.ilya.MeetingMap.Map.Server_API.WebSocketClient

import MarkerData
import android.util.Log
import com.google.firebase.auth.UserInfo
import kotlinx.serialization.Serializable
import okhttp3.WebSocket



sealed class WebSocketResponse {
    data class MarkersResponse(val markers: List<MarkerData>) : WebSocketResponse()
    data class UserInfoResponse(val userInfo: UserInfo) : WebSocketResponse()
    data class ChatMessage(val message: String, val sender: String) : WebSocketResponse()
    data class ErrorResponse(val error: String) : WebSocketResponse()
    data class UnknownResponse(val rawText: String) : WebSocketResponse()
}

interface WebSocketCommand {
    fun execute(webSocket: WebSocket)
}

class GetParticipantMarkCommand(private val uid: String, private val userKey: String) :
    WebSocketCommand {
    override fun execute(webSocket: WebSocket) {
        val command = "get_participant_mark $uid $userKey"
        webSocket.send(command)
        Log.d("WebSocket", "Sent: $command")
    }
}

class GetMarkCommand(private val uid: String, private val latitude: Double, private val longitude: Double) :
    WebSocketCommand {
    override fun execute(webSocket: WebSocket) {
        val command = "get_mark $uid $latitude $longitude"
        webSocket.send(command)
        Log.d("WebSocket", "Sent: $command")
    }
}



