package com.ilya.meetmapkmp.SocialMap.ViewModel


import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ilya.meetmapkmp.SocialMap.DATAServices.Chat_Service.ChatWebSocketService
import com.ilya.meetmapkmp.SocialMap.DataModel.DeleteMessage
import com.ilya.meetmapkmp.SocialMap.DataModel.DeleteMessageContent

import com.ilya.meetmapkmp.SocialMap.DataModel.Messageformat
import com.ilya.meetmapkmp.SocialMap.DataModel.Messages_Chat
import com.ilya.meetmapkmp.SocialMap.Interface.MyDataProvider
import com.ilya.platform.DriverFactory
import com.ilya.platform.di.ChatQueriesImpl
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val _sendToServer = MutableStateFlow<List<String>>(emptyList())
    val sendToServer: StateFlow<List<String>> get() = _sendToServer
    private var navController: NavController? = null
    var myroomId = MyDataProvider(context).getToken()
    private val chatService = ChatWebSocketService()

    // Установка NavController
    fun setNavController(controller: NavController) {
        navController = controller
    }

    // Навигация на определённый экран
    fun navigateTo(route: String) {
        navController?.navigate(route) {
            launchSingleTop = true // Не создавать дубли экрана
        }
    }
    // Возврат назад
    fun navigateBack() {
        navController?.popBackStack()
    }

    // Добавление строки в список sendToServer
    fun addToSendToServer(message: String) {
        viewModelScope.launch {
            val updatedList = _sendToServer.value + message
            _sendToServer.emit(updatedList)

            // Логирование добавленного сообщения и всего обновленного списка
            Log.d("ChatViewModel", "Добавлено сообщение: $message")
            Log.d("ChatViewModel", "Текущий список sendToServer: $updatedList")
        }
    }


    // Удаление строки из списка sendToServer
    fun removeFromSendToServer(message: String) {
        viewModelScope.launch {
            val updatedList = _sendToServer.value.filterNot { it == message }
            _sendToServer.emit(updatedList)
        }
    }


    // Функция для удаления нескольких сообщений
    suspend fun delete_from_local_db(chatId: String, messageIds: List<String>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    database.beginTransaction()
                    messageIds.forEach { messageId ->
                        chatQueries.deleteMessageById(chatId, messageId)
                    }
                    database.setTransactionSuccessful()
                } catch (e: Exception) {
                    Log.e("ChatViewModel", "Ошибка при удалении сообщений из чата: $chatId", e)
                } finally {
                    database.endTransaction()
                }
                // Обновление StateFlow после завершения операций с базой данных
                val allMessages = chatQueries.getAllMessages(chatId)
                _messages.emit(allMessages)
                Log.d(
                    "ChatViewModel",
                    "StateFlow обновлено после удаления сообщений из чата: $chatId"
                )

                // Также удаляем сообщения из WebSocket сервиса
                chatService.deleteMessagesByIds(messageIds)
            }
        }
    }




    // Очистка списка sendToServer
    fun clearSendToServer() {
        viewModelScope.launch {
            _sendToServer.emit(emptyList())
        }
    }

    // Получение текущего списка sendToServer
    fun getSendToServer(): List<String> {
        return _sendToServer.value
    }



    // Подключение к чату
    fun connectToChat(roomId: String, uid: String, key: String, name: String) {
        // Подключение к WebSocket
        chatService.connectToWebSocket("wss://meetmap.up.railway.app/chat/$roomId?username=$name&uid=$uid&key=$key")

        viewModelScope.launch {
            // Создание таблицы сообщений
            val tableName = "chat_$roomId"
            withContext(Dispatchers.IO) {
                driverFactory.createMessageTable(tableName)
            }

            // Обработка новых сообщений
            launch {
                chatService.messages.collect { newMessages ->
                    handleNewMessages(roomId, newMessages)
                }
            }

            // Обработка удаленных сообщений
            launch {
                chatService.deletemessages.collect { deletedMessages ->
                    handleDeletedMessages(roomId, deletedMessages)
                }
            }
        }
    }

    private suspend fun handleNewMessages(roomId: String, newMessages: List<Messages_Chat>) {
        withContext(Dispatchers.IO) {
            newMessages.forEach { message ->
                chatQueries.insertMessage(roomId, message)
                Log.d("GOTChatViewModel", "Message inserted into DB: $message")
            }
            val allMessages = chatQueries.getAllMessages(roomId)
            Log.d("GOTChatViewModel", "All messages from DB: $allMessages")
            _messages.emit(allMessages)
            Log.d("GOTChatViewModel", "StateFlow updated with messages from DB")
        }
    }

    private suspend fun handleDeletedMessages(roomId: String, deletedMessages: List<DeleteMessage>) {
        withContext(Dispatchers.IO) {
            deletedMessages.forEach { messageBatch ->
                messageBatch.delete_mesage?.forEach { messageId ->
                    chatQueries.deleteMessageById(roomId, messageId)

                    Log.d("ChatViewModel", "Deleting message with ID: $messageId")
                }
            }
            chatService.deleteMessagesByIds(deletedMessages.flatMap { it.delete_mesage ?: emptyList() })
            val allMessages = chatQueries.getAllMessages(roomId)

            if (allMessages.isEmpty()) {
                Log.w("с", "Сообщений в базе данных нет")
            }
            _messages.emit(allMessages)
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

    fun removeDeletedMessages(deleteMessages: List<String>) {
        _messages.value = _messages.value.filter { it.messageId !in deleteMessages }
    }



    fun sendDeleteMessage(deleteMessages: List<String>) {
        val message = DeleteMessageContent(deleteMessages)
        val jsonMessage = Json.encodeToString(message)
        chatService.sendMessage(jsonMessage)
        removeDeletedMessages(deleteMessages)
        viewModelScope.launch(Dispatchers.IO) {
            deleteMessages.forEach { messageId ->
                chatQueries.deleteMessageById(myroomId.toString(), messageId)
            }
            clearSendToServer()
        }
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