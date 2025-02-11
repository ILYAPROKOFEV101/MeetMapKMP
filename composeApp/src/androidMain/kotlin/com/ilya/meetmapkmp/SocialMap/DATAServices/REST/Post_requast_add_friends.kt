import android.util.Log
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.Retrofit

import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory

suspend fun postRequestAddFriends(uid: String, key: String, friendKey: String): String? {
    return try {
        // Логирование начала выполнения функции
        Log.d("PostRequestAddFriends", "Starting postRequestAddFriends with uid=$uid, key=$key, friendKey=$friendKey")

        // Создаем экземпляр Gson
        val gson = GsonBuilder().setLenient().create()

        // Создаем Retrofit-клиент с поддержкой Gson
        val retrofit = Retrofit.Builder()
            .baseUrl("https://meetmap.up.railway.app/")
            .addConverterFactory(GsonConverterFactory.create(gson)) // Используем Gson
            .build()

        // Логирование URL запроса
        val url = "https://meetmap.up.railway.app/friendrequest/$uid/$key/$friendKey"
        Log.d("PostRequestAddFriends", "Request URL: $url")

        // Создаем интерфейс для запроса
        val service = retrofit.create(PostInvite::class.java)
        Log.d("PostRequestAddFriends", "Retrofit service created successfully")

        // Выполняем запрос
        Log.d("PostRequestAddFriends", "Executing POST request...")
        val response = service.postInvite(uid, key, friendKey)

        // Проверяем успешность запроса
        if (response.isSuccessful) {
            Log.d("PostRequestAddFriends", "POST request successful. Response code: ${response.code()}")
            // Получаем тело ответа как объект TokenResponse
            val responseBody = response.body()
            if (responseBody != null) {
                Log.d("PostRequestAddFriends", "Response body received: $responseBody")
                // Извлекаем токен из объекта TokenResponse
                val token = responseBody.token
                Log.d("PostRequestAddFriends", "Token extracted successfully: $token")
                return token
            } else {
                // Если тело ответа пустое
                Log.e("PostRequestAddFriends", "Response body is null")
                return null
            }
        } else {
            // Обработка ошибки HTTP
            Log.e("PostRequestAddFriends", "HTTP error: ${response.code()}")
            Log.e("PostRequestAddFriends", "Error message: ${response.errorBody()?.string()}")
            return null
        }
    } catch (e: Exception) {
        // Логирование исключения
        Log.e("PostRequestAddFriends", "Exception occurred: ${e.message}")
        e.printStackTrace()
        return null
    }
}