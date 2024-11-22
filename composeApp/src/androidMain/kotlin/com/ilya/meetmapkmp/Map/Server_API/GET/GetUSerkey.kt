import android.content.Context
import com.ilya.reaction.logik.PreferenceHelper.setUserKey
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


// Функция для отправки GET-запроса с использованием OkHttp
suspend fun sendGetRequest(uid: String, client: OkHttpClient, context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            // Создание запроса
            val request = Request.Builder()
                .url("https://meetmap.up.railway.app/checkUser/$uid")
                .build()

            // Выполнение запроса и получение ответа
            val response: Response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: ""

                // Переход на главный поток для работы с UI
                withContext(Dispatchers.Main) {
                    println(responseBody)
                    setUserKey(context, responseBody)
                }
            } else {
                // Обработка ошибки, если запрос не был успешным
                println("Error: ${response.code} ${response.message}")
            }

        } catch (e: IOException) {
            // Обработка исключений ввода-вывода
            println("Error: ${e.message}")
        }
    }
}
