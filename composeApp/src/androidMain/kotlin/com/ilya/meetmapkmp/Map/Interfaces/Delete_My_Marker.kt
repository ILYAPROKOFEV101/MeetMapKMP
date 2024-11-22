package com.ilya.MeetingMap.Map.Interfaces

import retrofit2.http.DELETE
import retrofit2.http.Path

interface Delete_My_Marker {
    @DELETE("/delete/mymarker/{markerid}/{uid}/{key}")
    suspend fun deleteMarker(
        @Path("uid") uid: String,
        @Path("key") key: String,
        @Path("marker_id") marker_id: String
    ): String
}