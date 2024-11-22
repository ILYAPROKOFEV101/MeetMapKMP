import android.util.Log
import com.ilya.MeetingMap.Map.Interfaces.Get_MY_Participant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit




// Функция для получения данных участников
suspend fun getParticipant(uid: String, key: String): List<MarkerData> {
    // Логирование для диагностики сетевых запросов
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Клиент с увеличенными тайм-аутами
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS) // время на подключение
        .readTimeout(30, TimeUnit.SECONDS)    // время на чтение ответа
        .writeTimeout(30, TimeUnit.SECONDS)   // время на отправку данных
        .build()

    // Инициализация Retrofit с клиентом OkHttp и URL
    val retrofit = Retrofit.Builder()
        .baseUrl("https://meetmap.up.railway.app/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Создание экземпляра интерфейса API
    val apiService = retrofit.create(Get_MY_Participant::class.java)

    // Логирование запроса для отладки
    Log.d("MarkerData_getParticipant", "URL запроса: https://meetmap.up.railway.app/get/participantmark/$uid/$key")

    // Выполнение запроса в фоновом потоке
    return withContext(Dispatchers.IO) {
        try {
            val markerData = apiService.getParticipant(uid, key)
            Log.d("MarkerData_getParticipant", "Полученные данные маркера: $markerData")
            markerData
        } catch (e: HttpException) {
            Log.e("MarkerData_getParticipant", "HTTP ошибка: ${e.code()}", e)
            throw e
        } catch (e: IOException) {
            Log.e("MarkerData_getParticipant", "Сетевая ошибка", e)
            throw e
        } catch (e: Exception) {
            Log.e("MarkerData_getParticipant", "Неизвестная ошибка", e)
            throw e
        }
    }
}
