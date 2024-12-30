import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream

fun createTempFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.png")
        val outputStream = FileOutputStream(tempFile)

        inputStream?.copyTo(outputStream)

        outputStream.close()
        inputStream?.close()

        tempFile
    } catch (e: Exception) {
        Log.e("createTempFileFromUri", "Ошибка создания временного файла: ${e.message}")
        e.printStackTrace()
        null
    }
}

