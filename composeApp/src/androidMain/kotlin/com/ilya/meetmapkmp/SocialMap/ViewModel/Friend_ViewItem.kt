package com.ilya.meetmapkmp.SocialMap.ViewModel


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.ilya.meetmapkmp.SocialMap.DataModel.Friend

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FriendsViewModel : ViewModel() {
    val friendsList = mutableStateListOf<Friend>() // Используем для хранения друзей



    // Функция для обновления данных друзей
    fun updateFriends(newFriends: List<Friend>) {
        viewModelScope.launch(Dispatchers.Main) {
            friendsList.clear()
            friendsList.addAll(newFriends)
        }
    }


}
