package com.ilya.Supabase

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class SupabaseStorage(private val httpClient: HttpClient, private val supabaseUrl: String, private val supabaseKey: String) {

    suspend fun uploadFile(bucket: String, path: String, fileBytes: ByteArray): String {
        val url = "$supabaseUrl/storage/v1/object/$bucket/$path"
        val response: HttpResponse = httpClient.post(url) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $supabaseKey")
                append(HttpHeaders.ContentType, ContentType.Application.OctetStream.toString())
            }
            setBody(fileBytes)
        }

        if (response.status.isSuccess()) {
            return "$supabaseUrl/storage/v1/object/public/$bucket/$path"
        } else {
            throw Exception("Ошибка загрузки файла: ${response.status}")
        }
    }
}
