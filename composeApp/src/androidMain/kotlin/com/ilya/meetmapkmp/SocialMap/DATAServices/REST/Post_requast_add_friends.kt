import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

suspend fun postRequestAddFriends(uid: String, key: String, friendKey: String): ResponseBody? {
    return try {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://meetmap.up.railway.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PostInvite::class.java)

        // Выполняем запрос и получаем результат
        val response = service.postInvite(uid, key, friendKey)

        if (response.isSuccessful) {
            response.body() // Возвращаем тело ответа
        } else {
            // Обработка ошибки
            null
        }
    } catch (e: Exception) {
        // Ловим возможные ошибки (например, отсутствие сети)
        e.printStackTrace()
        null
    }
}
