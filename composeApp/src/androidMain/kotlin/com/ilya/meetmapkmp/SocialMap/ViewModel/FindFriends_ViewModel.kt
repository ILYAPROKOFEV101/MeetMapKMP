package com.ilya.meetmapkmp.SocialMap.ViewModel

import com.ilya.MeetingMap.SocialMap.DataModel.FindFriends
import Websocket_find_friends
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilya.meetmapkmp.SocialMap.DATAServices.WebSocketService
import com.ilya.meetmapkmp.SocialMap.DataModel.Friend
import com.ilya.platform.DriverFactory
import com.ilya.platform.di.FriendsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import java.util.concurrent.TimeUnit

class WebSocketViewModel : ViewModel() {

    private val context = AppContextProvider.getContext()

    private val friendsRepository = FriendsRepository(
        SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath("friends.db"), null)
    )


    // StateFlow для хранения списка друзей
    private val _friendsList = MutableStateFlow<List<FindFriends>>(emptyList())
    val friendsList: StateFlow<List<FindFriends>> = _friendsList.asStateFlow()


    // StateFlow для хранения ошибок
    private val _errors = MutableStateFlow("")
    val errors: StateFlow<String> = _errors.asStateFlow()

    // Инициализация WebSocketManager
    private val webSocketManager = Websocket_find_friends()



    init {
        DriverFactory(context).createFriendsTable()
        // Установка callback-функций для обработки сообщений и ошибок
        webSocketManager.setOnMessageReceivedListener { message ->
            viewModelScope.launch {
                parseFriendList(message)
            }
        }

        webSocketManager.setOnErrorOccurredListener { error ->
            viewModelScope.launch {
                _errors.value = error
            }
        }
    }

    // Метод для подключения к WebSocket
    fun connect(uid: String, key: String) {
        webSocketManager.connect(uid, key)
    }

    // Метод для закрытия WebSocket соединения
    fun disconnect() {
        webSocketManager.disconnect()
    }

    fun addfriends_to_bd(friend: Friend){
        friendsRepository.insertOrUpdateFriend(friend)
    }


    // Метод для отправки команды по WebSocket
    fun sendCommand(command: String) {
        webSocketManager.sendCommand(command)
    }

    // Метод для парсинга полученного списка друзей
    private fun parseFriendList(json: String) {
        try {
            Log.d("WebSocket_friends", "Парсинг списка друзей")
            val jsonArray = JSONArray(json)
            val friendsList = mutableListOf<FindFriends>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val friend = FindFriends(
                    key = jsonObject.getString("key"),
                    name = jsonObject.getString("name"),
                    img = jsonObject.getString("img"),
                    friend = jsonObject.getBoolean("friend")
                )
                friendsList.add(friend)
            }
            Log.d("WebSocket_friends", "Получен список друзей: ${friendsList.size} друзей")
            _friendsList.value = friendsList
        } catch (e: Exception) {
            Log.e("WebSocket_friends", "Ошибка парсинга JSON: ${e.message}")
            _errors.value = "Ошибка парсинга JSON: ${e.message}"
        }
    }

    // Метод для получения текущего списка друзей
    fun getCurrentFriendsList(): List<FindFriends> {
        return _friendsList.value
    }

    override fun onCleared() {
        super.onCleared()
        // Закрываем WebSocket при уничтожении ViewModel
        disconnect()
    }
}