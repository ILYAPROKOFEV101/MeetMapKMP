import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

// Вспомогательная функция для создания временного файла из Uri
fun createTempFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "temp_image")
        val outputStream = FileOutputStream(tempFile)

        inputStream?.copyTo(outputStream)

        outputStream.close()
        inputStream?.close()

        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}