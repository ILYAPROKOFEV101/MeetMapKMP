import android.util.Log
import com.google.android.datatransport.BuildConfig
import com.ilya.meetmapkmp.Map.Interfaces.Get_MY_Participant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit



object RetrofitClient {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://meetmap.up.railway.app/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: Get_MY_Participant = retrofit.create(Get_MY_Participant::class.java)
}


suspend fun getParticipant(uid: String, key: String): List<MarkerData> {
    return withContext(Dispatchers.IO) {
        try {
            Log.d("MarkerData_getParticipant", "Выполняем запрос: https://meetmap.up.railway.app/get/participantmark/$uid/$key")
            val markerData = RetrofitClient.apiService.getParticipant(uid, key)
            Log.d("MarkerData_getParticipant", "Полученные данные маркера: $markerData")
            markerData
        } catch (e: HttpException) {
            Log.e("MarkerData_getParticipant", "HTTP ошибка: ${e.code()}", e)
            emptyList() // Возвращаем пустой список или перекидываем исключение
        } catch (e: IOException) {
            Log.e("MarkerData_getParticipant", "Сетевая ошибка", e)
            emptyList() // Возвращаем пустой список или перекидываем исключение
        } catch (e: Exception) {
            Log.e("MarkerData_getParticipant", "Неизвестная ошибка", e)
            emptyList() // Возвращаем пустой список или перекидываем исключение
        }
    }
}
