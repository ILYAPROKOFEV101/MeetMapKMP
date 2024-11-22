package com.ilya.MeetingMap.Map.Server_API.GET

import android.util.Log
import com.ilya.MeetingMap.Map.Interfaces.NominatimService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

suspend fun getAddressFromCoordinates(latitude: Double, longitude: Double): String? {
    // Создание Retrofit экземпляра
    val retrofit = Retrofit.Builder()
        .baseUrl("https://nominatim.openstreetmap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Создание сервиса
    val service = retrofit.create(NominatimService::class.java)

    return try {
        // Запрос на получение адреса
        val response = service.getAddress(latitude, longitude)
        // Возврат названия улицы, если оно существует
        response.address.road ?: "Street not found"
    } catch (e: Exception) {
        // Логирование ошибки
        Log.e("NominatimResponse", "Error getting street: ${e.message}", e)
        null
    }
}