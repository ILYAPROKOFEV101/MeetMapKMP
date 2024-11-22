package com.ilya.MeetingMap.SocialMap.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.ilya.MeetingMap.SocialMap.DataModel.Messageformat
import com.ilya.MeetingMap.SocialMap.DataModel.Messages
import com.ilya.meetmapkmp.SocialMap.DATAServices.Chat_Service.ChatWebSocketService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<Messages>>(emptyList())
    val messages: StateFlow<List<Messages>> get() = _messages

    private val chatService = ChatWebSocketService()

    fun connectToChat(roomId: String, uid: String, key: String, name: String) {
        chatService.connectToWebSocket("wss://meetmap.up.railway.app/chat/$roomId?username=Ilya&uid=$uid&key=$key")

        viewModelScope.launch {
            chatService.messages.collect { newMessages ->
                _messages.value = newMessages
            }
        }
    }


    fun sendMessage(content: String, imageUrls: List<String>, videoUrls: List<String>, gifUrls: List<String>, fileUrls: List<String>) {
        val message = Messageformat(content, imageUrls, videoUrls, gifUrls, fileUrls)

        // Сериализуем объект Messageformat в строку JSON
        val jsonMessage = Json.encodeToString(message)

        // Отправка через WebSocket
        chatService.sendMessage(jsonMessage)
    }

    fun disconnectFromChat() {
        chatService.disconnect()
    }
}

