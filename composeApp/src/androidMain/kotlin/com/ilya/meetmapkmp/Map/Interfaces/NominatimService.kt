package com.ilya.MeetingMap.Map.Interfaces

import com.ilya.MeetingMap.Map.DataModel.NominatimResponse
import retrofit2.http.GET
import retrofit2.http.Query

// Интерфейс для Retrofit
interface NominatimService {
    @GET("reverse")
    suspend fun getAddress(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("format") format: String = "json"
    ): NominatimResponse
}