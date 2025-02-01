package com.ilya.meetmapkmp.Map.ViewModel

import MapMarker
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.ilya.MeetingMap.Map.Server_API.GET.getPublicMarker
import com.ilya.meetmapkmp.Map.DB.convertMarkerListToMapMarkerList
import com.ilya.platform.di.DatabaseHelper
import com.ilya.platform.di.MarkersQueriesImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val databaseHelper = DatabaseHelper(application)
    private val markersQueries = MarkersQueriesImpl(databaseHelper.writableDatabase)
    private val _markers = MutableStateFlow<List<MapMarker>>(emptyList())
    val markers: StateFlow<List<MapMarker>> get() = _markers

    // Функция для получения маркеров из базы данных и сервера
    fun fetchMarkers(uid: String, latLng: LatLng) {
        viewModelScope.launch {
            try {
                // Шаг 1: Получение локальных маркеров из базы данных
                val localMarkers = markersQueries.getAllMarkers()

                // Шаг 2: Получение маркеров с сервера
                val serverMarkers = getPublicMarker(uid, latLng)

                // Шаг 3: Объединение данных (если необходимо исключать дубликаты, можно сделать это здесь)
                val combinedMarkers = mergeMarkers(convertMarkerListToMapMarkerList(localMarkers), serverMarkers)

                // Шаг 4: Обновление StateFlow
                _markers.value = combinedMarkers


            } catch (e: Exception) {
                // Логирование ошибки
                Log.e("MapViewModel", "Ошибка получения маркеров", e)
            }
        }
    }




    // Функция для объединения маркеров, исключая дубликаты (если требуется)
    private fun mergeMarkers(
        localMarkers: List<MapMarker>,
        serverMarkers: List<MapMarker>
    ): List<MapMarker> {
        // Например, использовать ID для проверки уникальности
        val localMarkerIds = localMarkers.map { it.id }.toSet()
        val uniqueServerMarkers = serverMarkers.filterNot { it.id in localMarkerIds }
        return localMarkers + uniqueServerMarkers
    }
}

