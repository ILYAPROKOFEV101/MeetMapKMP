package com.ilya.meetmapkmp.SocialMap.ViewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ilya.meetmapkmp.SocialMap.DATAServices.WebSocketService
import com.ilya.meetmapkmp.SocialMap.DataModel.Friend
import com.ilya.platform.DriverFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class WebSocket_getfriendsViewModel(context: Context) : ViewModel() {

    companion object {
        private const val TAG = "WebSocketViewModel" // Тег для логов
    }

    // StateFlow для хранения состояния WebSocket
    private val _webSocketState = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected)
    val webSocketState: StateFlow<WebSocketState> = _webSocketState.asStateFlow()
    private val driverFactory = DriverFactory(context)





    // StateFlow для хранения списка друзей
    private val _friends = MutableStateFlow<List<Friend>>(emptyList())
    val friends: StateFlow<List<Friend>> = _friends.asStateFlow()

    private val webSocketService = WebSocketService(
        onMessageReceived = { jsonString ->
            Log.d(TAG, "Message received from server: $jsonString")
            // Парсим JSON в список Friend и обновляем StateFlow
            parseFriendFromJson(jsonString)?.let { newFriends ->
                Log.d(TAG, "Parsed ${newFriends.size} friends from JSON")
                _friends.value = newFriends
            }
        },
        onErrorOccurred = { error ->
            Log.e(TAG, "Error occurred: $error")
            // Обработка ошибок
            _webSocketState.value = WebSocketState.Error(error)
        }
    )


    init{
        driverFactory.createFriendsTable()
    }


    // Подключение к WebSocket
    fun connect(uid: String, key: String) {
        if (_webSocketState.value is WebSocketState.Connected) {
            Log.w(TAG, "Already connected. Ignoring new connection attempt.")
            return // Уже подключены
        }
        Log.d(TAG, "Attempting to connect with uid: $uid, key: $key")
        _webSocketState.value = WebSocketState.Connecting
        webSocketService.connect(uid, key)
        _webSocketState.value = WebSocketState.Connected
        Log.d(TAG, "WebSocket connection established.")
    }

    // Отключение от WebSocket
    fun disconnect() {
        Log.d(TAG, "Disconnecting WebSocket...")
        webSocketService.disconnect()
        _webSocketState.value = WebSocketState.Disconnected
        Log.d(TAG, "WebSocket disconnected.")
    }

    // Отправка команды через WebSocket
    fun sendCommand(command: String) {
        if (_webSocketState.value is WebSocketState.Connected) {
            Log.d(TAG, "Sending command: $command")
            webSocketService.sendCommand(command)
        } else {
            Log.e(TAG, "WebSocket is not connected. Cannot send command: $command")
            _webSocketState.value = WebSocketState.Error("WebSocket is not connected")
        }
    }

    // Функция для парсинга JSON в объект Friend
    private fun parseFriendFromJson(jsonString: String): List<Friend>? {
        return try {
            Log.d(TAG, "Parsing JSON string into Friend objects...")
            Json.decodeFromString<List<Friend>>(jsonString)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse JSON: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    // Состояния WebSocket
    sealed class WebSocketState {
        object Connecting : WebSocketState()
        object Connected : WebSocketState()
        object Disconnected : WebSocketState()
        data class Error(val message: String) : WebSocketState()
    }
}

class ContextViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WebSocket_getfriendsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WebSocket_getfriendsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

