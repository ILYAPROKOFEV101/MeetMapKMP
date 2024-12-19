package com.ilya.meetmapkmp.Map.ViewModel

import MapMarker
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.ilya.MeetingMap.Map.Server_API.GET.getPublicMarker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    private val _markers = MutableStateFlow<List<MapMarker>>(emptyList())
    val markers: StateFlow<List<MapMarker>> get() = _markers

    // Функция для получения маркеров и обновления StateFlow
    fun fetchMarkers(uid: String, latLng: LatLng) {
        viewModelScope.launch {
            try {
                val markerList = getPublicMarker(uid, latLng)
                _markers.value = markerList
            } catch (e: Exception) {
                // Логирование ошибки
                Log.e("MapViewModel", "Ошибка получения маркеров", e)
            }
        }
    }
}
