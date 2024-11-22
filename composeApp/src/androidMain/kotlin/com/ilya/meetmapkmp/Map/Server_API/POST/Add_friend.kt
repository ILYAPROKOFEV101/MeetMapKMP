package com.ilya.MeetingMap.Map.Server_API.POST

import AddFriendsService
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


suspend fun addFriends(uid: String, key: String, friendKey: String) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://meetmap.up.railway.app/")
        .build()

    val apiService = retrofit.create(AddFriendsService::class.java)
    Log.d("AddFriendsService", "URL: https://meetmap.up.railway.app/addFriends/$uid/$key/$friendKey")

    apiService.addFriends(uid, key, friendKey).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                Log.d("AddFriendsService", "Data successfully pushed to server")
            } else {
                val errorBody = response.errorBody()?.string() // Получение тела ошибки
                Log.e("AddFriendsService", "Error response: ${response.code()}. Details: $errorBody")
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Log.e("AddFriendsService", "Request failed: ${t.message}. Stacktrace: ${Log.getStackTraceString(t)}")
        }
    })
}
