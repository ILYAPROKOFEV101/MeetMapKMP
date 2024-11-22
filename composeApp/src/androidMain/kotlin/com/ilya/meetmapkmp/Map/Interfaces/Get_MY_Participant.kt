package com.ilya.MeetingMap.Map.Interfaces

import MarkerData
import retrofit2.http.GET
import retrofit2.http.Path

interface Get_MY_Participant {
    @GET("/get/participantmark/{uid}/{key}")
    suspend fun getParticipant(
        @Path("uid") uid: String,
        @Path("key") key: String
    ): List<MarkerData>
}