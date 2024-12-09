package com.ilya.Supabase



suspend fun uploadPhotoToSupabase() {
    val httpClient = createHttpClient()
    val supabaseStorage = SupabaseStorage(
        httpClient = httpClient,
        supabaseUrl = "https://imlhstamcqwacpgldxsf.supabase.co/storage/v1/s3",
        supabaseKey = "533d9e6301a67a9c1aa533386a6d92f8"
    )

    val bucket = "my-bucket"
    val path = "photos/my-photo.jpg"

    try {
        val fileBytes = getFileBytes() // Функция для получения байтов файла
        val fileUrl = supabaseStorage.uploadFile(bucket, path, fileBytes)
        println("Файл успешно загружен: $fileUrl")
    } catch (e: Exception) {
        println("Ошибка загрузки файла: ${e.message}")
    } finally {
        httpClient.close()
    }
}
