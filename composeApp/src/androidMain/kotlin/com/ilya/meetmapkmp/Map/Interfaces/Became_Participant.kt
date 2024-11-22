package com.ilya.MeetingMap.Map.Interfaces

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface Became_Participant {

    @POST("/became/participant/{uid}/{key}/{id}")
    suspend fun Became_Participant(
        @Path("uid") uid: String,
        @Path("key") key: String,
        @Path("id") id: String
    ): Response<ResponseBody>  // Возвращаем Response<ResponseBody>
}