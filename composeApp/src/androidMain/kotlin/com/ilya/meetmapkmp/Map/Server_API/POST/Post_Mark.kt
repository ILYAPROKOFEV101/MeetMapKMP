package com.ilya.MeetingMap.Map.Server_API.POST

import MarkerData
import android.util.Log
import com.ilya.MeetingMap.Map.Interfaces.Post_Mark
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun postInvite(key: String, uid: String, markerData: MarkerData) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://meetmap.up.railway.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(Post_Mark::class.java)

    val call = apiService.postMarker(uid, key, markerData)
    call.enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                Log.d("PushDataJoin", "Data successfully pushed to server")
            } else {
                Log.e("PushDataJoin", "Failed to push data to server. Error code: ${response.code()}")
                Log.e("PushDataJoin", "Error body: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Log.e("PushDataJoin", "Failed to push data to server. Error message: ${t.message}")
        }
    })
}


