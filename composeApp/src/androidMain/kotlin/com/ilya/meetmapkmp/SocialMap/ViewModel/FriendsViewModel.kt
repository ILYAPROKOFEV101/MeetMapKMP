package com.ilya.MeetingMap.SocialMap.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ilya.MeetingMap.SocialMap.DataModel.FindFriends


class FriendsViewModel_data : ViewModel() {
    private val _friendsList = mutableStateOf(emptyList<FindFriends>())
    val friendsList: State<List<FindFriends>> get() = _friendsList

    var username by mutableStateOf("")
        private set // Делаем сеттер приватным, чтобы предотвратить изменение извне

    fun updateUsername(newUsername: String) {
        username = newUsername
    }

    fun updateFriendsList(friends: List<FindFriends>) {
        _friendsList.value = friends
    }
}
