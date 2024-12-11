package com.ilya.meetmapkmp.SocialMap.DATAServices.Storeg

import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

import android.util.Log


suspend fun createBucketAndUploadPhoto(
    bucketName: String,
    fileName: String,
    file: File,
    maxFileSizeMB: Int = 5
): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            Log.d("createBucketAndUploadPhoto", "Создание бакета с именем: $bucketName")



            Log.d("createBucketAndUploadPhoto", "Бакет $bucketName успешно создан")

            // Получение бакета
            val bucket = supabase.storage.from(bucketName)
            Log.d("createBucketAndUploadPhoto", "Получен бакет $bucketName")

            // Загрузка файла (предварительно читаем файл в ByteArray)
            val fileBytes = file.readBytes()
            Log.d("createBucketAndUploadPhoto", "Чтение файла ${file.name}, размер: ${fileBytes.size} байт")

            bucket.upload(fileName, fileBytes) {
                upsert = false
            }

            Log.d("createBucketAndUploadPhoto", "Файл $fileName успешно загружен в бакет $bucketName")

            true // Успешно
        } catch (e: Exception) {
            Log.e("createBucketAndUploadPhoto", "Ошибка при создании бакета или загрузке файла", e)
            e.printStackTrace()
            false // Ошибка
        }
    }
}
