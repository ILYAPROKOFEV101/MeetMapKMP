package com.ilya.MeetingMap.Map.Interfaces

import MapMarker
import retrofit2.http.GET
import retrofit2.http.Path

// Интерфейс для получения маркеров
interface GetPublicMark {
    @GET("/get/public/mark/{uid}/{lat}/{lon}")
    suspend fun getMarker(
        @Path("uid") uid: String,
        @Path("lat") lat: Double,
        @Path("lon") lon: Double
    ): List<MapMarker>
}
