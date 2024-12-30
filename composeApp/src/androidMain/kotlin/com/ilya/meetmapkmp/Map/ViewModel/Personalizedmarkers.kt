package com.ilya.meetmapkmp.Map.ViewModel

import MarkerData
import androidx.lifecycle.ViewModel
import com.ilya.meetmapkmp.Map.Server_API.DELETE.deleteParticipantMarker
import getParticipant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class PersonalizedMarkers : ViewModel() {

    private var markerList: MutableList<MarkerData> = mutableListOf() // Список меток, хранящийся в памяти

    // Функция для получения меток — сначала проверяем память, если нет — делаем запрос на сервер
    suspend fun getMarkerList(uid: String, key: String): List<MarkerData> {
        return if (markerList.isNotEmpty()) {
            // Если метки есть в памяти, возвращаем их
            markerList
        } else {
            // Если меток нет в памяти, загружаем их с сервера
            getMarkerList_From_Server(uid, key).also { serverMarkers ->
                // После получения меток с сервера добавляем их в память
                markerList.addAll(serverMarkers)
            }
        }
    }

    // Функция для получения меток с сервера
    suspend fun getMarkerList_From_Server(uid: String, key: String): List<MarkerData> {
        return withContext(Dispatchers.IO) {
            getParticipant(uid, key) // Метод для получения меток с сервера
        }
    }

    // Функция для добавления метки в память
    fun addMarker(marker: MarkerData) {
        markerList.add(marker)
    }

    // Функция для удаления метки из памяти
    fun removeMarker(marker: MarkerData) {
        markerList.remove(marker)
    }

    fun deleteParticipantMarkerFromMemory(id: String){
        markerList.removeIf { it.id == id }
    }


    suspend fun deleteParticipantMarker(uid: String, key: String, id: String) : Boolean{
        return withContext(Dispatchers.IO){
            deleteParticipantMarker(uid, key, id)
        }
    }




}
