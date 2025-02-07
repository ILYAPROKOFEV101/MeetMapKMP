package com.ilya.meetmapkmp.Map.ViewModel

import MarkerData
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilya.Database
import com.ilya.meetmapkmp.Map.Server_API.DELETE.deleteParticipantMarker
import com.ilya.platform.DriverFactory
import com.ilya.platform.di.DatabaseHelper
import com.ilya.platform.di.Marker
import com.ilya.platform.di.MarkersQueriesImpl
import getParticipant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PersonalizedMarkersViewModel(
    private val context: Context
) : ViewModel() {


    private val databaseHelper = DatabaseHelper(context)
    private val markersQueries = MarkersQueriesImpl(databaseHelper.writableDatabase)

    // Используем StateFlow для отслеживания изменений маркеров
    private val _markerList = MutableStateFlow<List<Marker>>(emptyList())
    val markerList: StateFlow<List<Marker>> get() = _markerList

    init {
        Log.d("PersonalizedMarkersVM", "ViewModel initialized")
        // Вызываем функцию для начальной загрузки маркеров
        refreshMarkerList()
    }

    // Добавление маркеров в базу данных
    fun addMarkers(markers: List<Marker>) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("PersonalizedMarkersVM", "Starting to add ${markers.size} markers")
            markers.forEach { marker ->
                Log.d("PersonalizedMarkersVM", "Adding marker: $marker")
                markersQueries.insertMarker(
                    marker.id,
                    marker.key,
                    marker.username,
                    marker.imguser,
                    marker.photomark,
                    marker.street,
                    marker.lat,
                    marker.lon,
                    marker.name,
                    marker.whatHappens,
                    marker.startDate,
                    marker.endDate,
                    marker.startTime,
                    marker.endTime,
                    marker.participants,
                    marker.access
                )
            }
            Log.d("PersonalizedMarkersVM", "Finished adding markers, refreshing marker list")
            // После добавления маркеров обновляем список
            refreshMarkerList()
            _markerList.value = getAllMarkers() // Обновляем список маркеров
        }
    }

    // Получение маркера по его ID
    fun getMarkerById(id: String): Marker? {
        Log.d("PersonalizedMarkersVM", "Fetching marker with id: $id")
        val marker = markersQueries.getMarkerById(id)
        if (marker != null) {
            Log.d("PersonalizedMarkersVM", "Marker found: $marker")
        } else {
            Log.d("PersonalizedMarkersVM", "No marker found with id: $id")
        }
        return marker
    }



    // Удаление маркера по ID
    fun deleteMarkerById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("PersonalizedMarkersVM", "Deleting marker with id: $id")
            markersQueries.deleteMarkerById(id)
            Log.d("PersonalizedMarkersVM", "Marker deleted, refreshing marker list")
            // После удаления маркера обновляем список
            refreshMarkerList()
        }
    }

    // Получение всех маркеров из базы данных
    suspend fun getAllMarkers(): List<Marker> {
        return withContext(Dispatchers.IO) {
            Log.d("PersonalizedMarkersVM", "Fetching all markers")
            val markers = markersQueries.getAllMarkers() // Получаем все маркеры асинхронно
            Log.d("PersonalizedMarkersVM", "Markers loaded: ${markers.size} items")
            markers
        }
    }

    // Обновление списка маркеров
    private fun refreshMarkerList() {
        viewModelScope.launch(Dispatchers.IO) {
            val markers = markersQueries.selectAllMarkers()
            _markerList.emit(markers)
        }
    }
}



class PersonalizedMarkersViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonalizedMarkersViewModel::class.java)) {
            return PersonalizedMarkersViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
