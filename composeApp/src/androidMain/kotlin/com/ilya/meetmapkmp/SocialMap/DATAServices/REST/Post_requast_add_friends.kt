import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

suspend fun postRequestAddFriends(uid: String, key: String, friendKey: String): String? {
    return try {
        // Создаем Retrofit-клиент
        val retrofit = Retrofit.Builder()
            .baseUrl("https://meetmap.up.railway.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Создаем интерфейс для запроса
        val service = retrofit.create(PostInvite::class.java)

        // Выполняем запрос
        val response = service.postInvite(uid, key, friendKey)

        // Проверяем успешность запроса
        if (response.isSuccessful) {
            // Получаем тело ответа как JSON
            val responseBody = response.body()
            if (responseBody != null) {
                // Извлекаем токен из JSON-ответа
                val token = responseBody.get("token")?.toString()
                return token
            } else {
                // Если тело ответа пустое
                null
            }
        } else {
            // Обработка ошибки HTTP
            Log.e("PostRequestAddFriends", "HTTP error: ${response.code()}")
            null
        }
    } catch (e: Exception) {
        // Ловим возможные ошибки (например, отсутствие сети)
        e.printStackTrace()
        null
    }
}