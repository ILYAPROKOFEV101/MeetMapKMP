package com.ilya.meetmapkmp.SocialMap.ViewModel


import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ilya.meetmapkmp.SocialMap.DATAServices.Chat_Service.ChatWebSocketService
import com.ilya.meetmapkmp.SocialMap.DataModel.DeleteMessage
import com.ilya.meetmapkmp.SocialMap.DataModel.DeleteMessageContent

import com.ilya.meetmapkmp.SocialMap.DataModel.Messageformat
import com.ilya.meetmapkmp.SocialMap.DataModel.Messages_Chat
import com.ilya.platform.DriverFactory
import com.ilya.platform.di.ChatQueriesImpl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChatViewModel(context: Application) : ViewModel() {

    private val driverFactory = DriverFactory(context)
    private val database = SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath("bucket.db"), null)
    private val chatQueries = ChatQueriesImpl(database)



    private val _messages = MutableStateFlow<List<Messages_Chat>>(emptyList())
    private val _deletemessages = MutableStateFlow<List<DeleteMessage>>(emptyList())

    val messages: StateFlow<List<Messages_Chat>> get() = _messages
    val deletemessages: StateFlow<List<DeleteMessage>> get() = _deletemessages

    private val chatService = ChatWebSocketService()
    // Подключение к чату
    fun connectToChat(roomId: String, uid: String, key: String, name: String) {
        chatService.connectToWebSocket("wss://meetmap.up.railway.app/chat/$roomId?username=$name&uid=$uid&key=$key")

        viewModelScope.launch {
            val tableName = "chat_$roomId"
            driverFactory.createMessageTable(tableName)
        }

        viewModelScope.launch {
            chatService.messages.collect { newMessages ->
                //  новые сообщения в базу данных
                newMessages.forEach { message ->
                    chatQueries.insertMessage(roomId, message)
                }

                // все сообщения из базы данных
                val allMessages = chatQueries.getAllMessages(roomId)

                // О StateFlow с данными из базы
                _messages.emit(allMessages)
            }
        }



        viewModelScope.launch {
            chatService.deletemessages.collect { deletedMessages ->
                _deletemessages.emit(deletedMessages)
            }
        }




        viewModelScope.launch {
            chatService.deletemessages.collect { deletedMessages ->
                deletedMessages.forEach { deletedMessage ->
                    deletedMessage.delete_mesage?.firstOrNull()?.let { messageId ->
                        chatQueries.deleteMessageById(roomId, messageId)
                    }
                }

                val allMessages = chatQueries.getAllMessages(roomId)
                if (allMessages.isEmpty()) {
                    Log.w("с", "Сообщений в базе данных нет")
                }
                _messages.emit(allMessages)
            }
        }

    }

    // Обработка удаления сообщений
    suspend fun handleDeleteMessages(deleteMessages: List<String>) {
        // Фильтруем список сообщений, удаляя те, которые присутствуют в списке удалённых сообщений
        val updatedMessages = _messages.value.filterNot { it.key in deleteMessages }
        _messages.emit(updatedMessages)
    }

    // Отправка нового сообщения
    fun sendMessage(content: String, imageUrls: List<String>, videoUrls: List<String>, gifUrls: List<String>, fileUrls: List<String>) {
        val message = Messageformat(content, gifUrls, imageUrls, videoUrls, fileUrls)

        // Сериализация объекта Messageformat в строку JSON
        val jsonMessage = Json.encodeToString(message)

        // Отправка через WebSocket
        chatService.sendMessage(jsonMessage)
    }

    // Отправка сообщений для удаления
    fun sendDeleteMessage(deleteMessages: List<String>) {
        val message = DeleteMessageContent(deleteMessages)
        // Сериализация объекта DeleteMessageContent в строку JSON
        val jsonMessage = Json.encodeToString(message)

        // Отправка через WebSocket
        chatService.sendMessage(jsonMessage)
    }


    // Отключение от чата
    fun disconnectFromChat() {
        chatService.disconnect()
    }
}


class ChatViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(context.applicationContext as Application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
