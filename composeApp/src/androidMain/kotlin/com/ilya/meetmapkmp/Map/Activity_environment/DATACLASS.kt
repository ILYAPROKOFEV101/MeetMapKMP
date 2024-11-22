import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.serialization.Serializable

data class MapProperties(val googleMap: com.google.android.gms.maps.GoogleMap)
data class MapUiSettings(val uiSettings: com.google.android.gms.maps.UiSettings)



// Класс для хранения состояния MapView
data class MapViewState(
    val userLocation: LatLng? = null,
    val userMarker: Marker? = null
)
@Serializable
data class MarkerData(
    val key: String,
    val username: String,
    val imguser: String,
    val photomark: String,
    val street: String,
    val id: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val whatHappens: String,
    val startDate: String,
    val endDate: String,
    val startTime: String,
    val endTime: String,
    val participants: Int,
    val access: Boolean
)

// для конвертации данных
fun markerDataToMapMarker(markerData: MarkerData): MapMarker {
    return MapMarker(
        key = markerData.key,
        username = markerData.username,
        imguser = markerData.imguser,
        photomark = markerData.photomark,
        street = markerData.street,
        id = markerData.id,
        lat = markerData.lat,
        lon = markerData.lon,
        name = markerData.name,
        whatHappens = markerData.whatHappens,
        startDate = markerData.startDate,
        endDate = markerData.endDate,
        startTime = markerData.startTime,
        endTime = markerData.endTime,
        participants = markerData.participants,
        access = markerData.access
    )
}
