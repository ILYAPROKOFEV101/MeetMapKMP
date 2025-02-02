package com.ilya.meetmapkmp.SocialMap.ViewModel


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.ilya.meetmapkmp.SocialMap.DataModel.Friend
import com.ilya.platform.DriverFactory
import com.ilya.platform.di.ChatQueriesImpl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FriendsViewModel() : ViewModel() {
    val friendsList = mutableStateListOf<Friend>() // Используем для хранения друзей

   // private val driverFactory = DriverFactory(context)
  //  private val database = SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath("friends.db"), null)
  //  private val chatQueries = ChatQueriesImpl(database)


    // Функция для обновления данных друзей
    fun updateFriends(newFriends: List<Friend>) {
        viewModelScope.launch(Dispatchers.Main) {
            friendsList.clear()
            friendsList.addAll(newFriends)
        }
    }


}
