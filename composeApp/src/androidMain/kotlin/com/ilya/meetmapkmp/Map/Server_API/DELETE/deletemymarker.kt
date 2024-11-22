import com.ilya.MeetingMap.Map.Interfaces.Delete_My_Marker
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// интерфейс для отправки delete запроса на сервре


//корутинная функция котруя я могу вызвать чтобы удолить метку с карты и из сервера .   git  
suspend fun deleteMyMarker(uid: String, key: String, marker_id: String): String {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://meetmap.up.railway.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(Delete_My_Marker::class.java)
    return apiService.deleteMarker(uid, key, marker_id)
}