import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.type.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MapMarkerManager {
    private var googleMap: GoogleMap? = null
    val markerDataMap = mutableMapOf<Marker, MarkerData>()

    fun setGoogleMap(map: GoogleMap) {
        googleMap = map
    }

    // Метод для удаления маркера
    fun removeSpecificMarker(markerData: MarkerData) {
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("RemoveMarker", "Попытка удалить маркер с id=${markerData.id}")
            Log.d("RemoveMarker", "Состояние markerDataMap до удаления: ${markerDataMap.values.map { it.id }}")

            val markerToRemove = markerDataMap.entries.find { it.value.id == markerData.id }
            markerToRemove?.let { entry ->
                // Удаляем маркер с карты
                Log.d("RemoveMarker", "Удаляем маркер с карты: ${entry.key.title}")
                entry.key.remove()

                // Удаляем маркер и его данные из коллекции
                markerDataMap.remove(entry.key)

                // Логируем успешное удаление
                Log.d("RemoveMarker", "Метка с id=${markerData.id} успешно удалена")
            } ?: Log.e("RemoveMarker", "Маркер с id=${markerData.id} не найден")
        }
    }

    // Метод для получения данных маркера
    fun getMarkerData(marker: Marker): MarkerData? {
        return markerDataMap[marker]
    }
}

