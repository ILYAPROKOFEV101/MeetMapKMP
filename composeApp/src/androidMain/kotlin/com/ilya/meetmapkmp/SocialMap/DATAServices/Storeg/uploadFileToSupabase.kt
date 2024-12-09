package com.ilya.meetmapkmp.SocialMap.DATAServices.Storeg


import android.widget.Toast
import com.ilya.Supabase.SupabaseStorage
import com.ilya.Supabase.createHttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import android.content.Context
import android.net.Uri
import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.io.InputStream



suspend fun uploadFileToSupabase(
    context: Context,
    uri: Uri,
    bucketName: String,
    supabaseUrl: String,
    supabaseKey: String
) {
    val httpClient = HttpClient()
    val contentResolver = context.contentResolver
    val inputStream: InputStream? = contentResolver.openInputStream(uri)
    val fileName = "${System.currentTimeMillis()}.jpg" // Убедитесь, что имя файла корректно

    inputStream?.let { stream ->
        try {
            // Прочитаем данные файла в байтовый массив
            val byteArray = stream.readBytes()

            val url = "$supabaseUrl/storage/v1/object/$bucketName/$fileName"

            // Отправим PUT запрос для загрузки файла
            val response: HttpResponse = httpClient.put(url) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $supabaseKey") // Добавляем ключ аутентификации
                    append(HttpHeaders.ContentType, "image/jpeg") // Устанавливаем тип контента
                }
                setBody(byteArray) // Отправляем байтовый массив файла
            }

            // Проверка успешности загрузки
            if (response.status.isSuccess()) {
                Log.d("SupabaseUpload", "File uploaded successfully: ${response.status}")
            } else {
                Log.e("SupabaseUpload", "Error uploading file: ${response.status}")
                Log.e("SupabaseUpload", "Response body: ${response.body<String>()}")
            }
        } catch (e: Exception) {
            Log.e("SupabaseUpload", "Exception: ${e.localizedMessage}")
        } finally {
            stream.close()
        }
    } ?: Log.e("SupabaseUpload", "InputStream is null")
}
