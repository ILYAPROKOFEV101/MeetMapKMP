package com.ilya.MeetingMap.Map.Interfaces

import MarkerData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface Post_Mark {
    @POST("/mark/{uid}/{key}")
    fun postMarker(
        @Path("uid") uid: String,
        @Path("key") key: String,
        @Body request: MarkerData
    ): Call<Void>
}
