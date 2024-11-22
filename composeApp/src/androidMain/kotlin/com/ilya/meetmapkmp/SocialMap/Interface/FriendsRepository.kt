package com.ilya.MeetingMap.SocialMap.Interface

import android.content.Context

// 1. Modify the interface
interface DataProvider {
    fun saveToken(token: String)
    fun getToken(): String?
}

class MyDataProvider(context: Context) : DataProvider {
    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    private var token: String? = null // Store the token in memory

    override fun saveToken(token: String) {
        this.token = token // Update the in-memory token
        sharedPreferences.edit().putString("token", token).apply() // Save to SharedPreferences
    }

    override fun getToken(): String? {
        // Возвращаем токен из памяти, если он там есть, иначе получаем из SharedPreferences
        return token ?: sharedPreferences.getString("token", null).also {
            token = it // Кэшируем токен в памяти для последующего быстрого доступа
        }
    }


}

