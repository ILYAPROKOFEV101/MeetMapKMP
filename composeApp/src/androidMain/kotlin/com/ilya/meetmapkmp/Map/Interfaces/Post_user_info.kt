package com.ilya.MeetingMap.Map.Interfaces

import Post_User_info_data
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface Post_user_info {
    @POST("/user/{uid}/{key}")
    fun postMarker(
        @Path("uid") uid: String,
        @Path("key") key: String,
        @Body request: Post_User_info_data
    ): Call<Void>
}