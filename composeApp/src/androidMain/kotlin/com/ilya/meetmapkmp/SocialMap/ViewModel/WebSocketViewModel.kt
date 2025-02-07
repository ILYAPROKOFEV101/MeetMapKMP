package com.ilya.meetmapkmp.SocialMap.ViewModel

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ilya.meetmapkmp.SocialMap.DATAServices.WebSocketService
import com.ilya.meetmapkmp.SocialMap.DataModel.Friend
import com.ilya.platform.DriverFactory
import com.ilya.platform.di.FriendsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
class WebSocket_getfriendsViewModel : ViewModel() {
    companion object {
        private const val TAG = "WebSocketViewModel"
    }

    private val context = AppContextProvider.getContext()

    // StateFlow для хранения состояния WebSocket
    private val _webSocketState = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected)
    val webSocketState: StateFlow<WebSocketState> = _webSocketState.asStateFlow()

    // StateFlow для хранения списка друзей
    private val _friends = MutableStateFlow<List<Friend>>(emptyList())
    val friends: StateFlow<List<Friend>> = _friends.asStateFlow()

    // FriendsRepository для работы с базой данных
    private val friendsRepository = FriendsRepository(
        SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath("friends.db"), null)
    )

    // WebSocketService для обработки WebSocket сообщений
    private val webSocketService = WebSocketService(
        onMessageReceived = { jsonString ->
            Log.d(TAG, "Message received from server: $jsonString")
            parseAndSaveFriends(jsonString)
        },
        onErrorOccurred = { error ->
            Log.e(TAG, "Error occurred: $error")
            _webSocketState.value = WebSocketState.Error(error)
        }
    )

    init {
        DriverFactory(context).createFriendsTable()

        // Загрузка начальных данных
        loadFriendsFromDatabase()

        // Подписка на изменения в базе данных
        friendsRepository.onDatabaseChanged = {
            loadFriendsFromDatabase()
        }
    }

    private fun loadFriendsFromDatabase() {
        viewModelScope.launch {
            try {
                val allFriends = friendsRepository.getAllFriends()
                _friends.value = allFriends
                Log.d(TAG, "Loaded ${allFriends.size} friends from database.")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading friends from database: ${e.message}")
            }
        }
    }

    fun connect(uid: String, key: String) {
        if (_webSocketState.value is WebSocketState.Connected) {
            Log.w(TAG, "Already connected. Ignoring new connection attempt.")
            return
        }
        Log.d(TAG, "Attempting to connect with uid: $uid, key: $key")
        _webSocketState.value = WebSocketState.Connecting
        webSocketService.connect(uid, key)
        _webSocketState.value = WebSocketState.Connected
        Log.d(TAG, "WebSocket connection established.")
    }

    fun disconnect() {
        Log.d(TAG, "Disconnecting WebSocket...")
        webSocketService.disconnect()
        _webSocketState.value = WebSocketState.Disconnected
        Log.d(TAG, "WebSocket disconnected.")
    }

    fun sendCommand(command: String) {
        if (_webSocketState.value is WebSocketState.Connected) {
            Log.d(TAG, "Sending command: $command")
            webSocketService.sendCommand(command)
        } else {
            Log.e(TAG, "WebSocket is not connected. Cannot send command: $command")
            _webSocketState.value = WebSocketState.Error("WebSocket is not connected")
        }
    }
    fun deletefriends_from_bd(token: String){
        friendsRepository.deleteFriendByToken(token)
    }



    private fun parseAndSaveFriends(jsonString: String) {
        viewModelScope.launch {
            try {
                val newFriends = Json.decodeFromString<List<Friend>>(jsonString)
                newFriends.forEach { friend ->
                    friendsRepository.insertOrUpdateFriend(friend)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse JSON or save friends: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    sealed class WebSocketState {
        object Connecting : WebSocketState()
        object Connected : WebSocketState()
        object Disconnected : WebSocketState()
        data class Error(val message: String) : WebSocketState()
    }
}