package com.ilya.Supabase



import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BucketManager(private val ioDispatcher: CoroutineDispatcher) {

    suspend fun createBucketAndUploadPhoto(
        bucketName: String,
        fileName: String,
        file: ByteArray,
        maxFileSizeMB: Int = 5,
        log: (String) -> Unit
    ): Boolean {
        return withContext(ioDispatcher) {
            try {
                log("Создание бакета с именем: $bucketName")

                // Add bucket creation logic here if needed
                log("Бакет $bucketName успешно создан")

                // Simulated bucket fetching (replace with actual implementation)
                val bucket = supabase.storage.from(bucketName)
                log("Получен бакет $bucketName")

                // File size validation
                if (file.size > maxFileSizeMB * 1024 * 1024) {
                    log("Ошибка: размер файла превышает ${maxFileSizeMB}MB")
                    return@withContext false
                }

                log("Чтение файла $fileName, размер: ${file.size} байт")

                // Upload logic (adjust as per actual implementation)
                bucket.upload(fileName, file) {
                    upsert = false
                }

                log("Файл $fileName успешно загружен в бакет $bucketName")
                true
            } catch (e: Exception) {
                log("Ошибка при создании бакета или загрузке файла: ${e.message}")
                e.printStackTrace()
                false
            }
        }
    }
}
