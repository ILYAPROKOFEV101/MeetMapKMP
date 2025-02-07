package com.ilya.platform.di

import android.database.sqlite.SQLiteDatabase
import com.ilya.meetmapkmp.SocialMap.DataModel.Messages_Chat

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatQueriesImpl(private val database: SQLiteDatabase) {

    // Лог-тег для удобства фильтрации
    private val TAG = "ChatQueriesImpl"

    // Добавление сообщения в таблицу
    fun insertMessage(chatId: String, message: Messages_Chat) {
        val query = """
            INSERT INTO chat_$chatId (
                message_id, content, profilerIMG, messageTime, key, senderUsername, gifUrls, imageUrls, videoUrls, fileUrls
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        Log.d(TAG, "Вставка сообщения в таблицу chat_$chatId: $message")
        try {
            database.execSQL(
                query, arrayOf(
                    message.messageId,
                    message.content,
                    message.profilerIMG,
                    message.messageTime,
                    message.key,
                    message.senderUsername,
                    message.gifUrls.joinToString(","),
                    message.imageUrls.joinToString(","),
                    message.videoUrls.joinToString(","),
                    message.fileUrls.joinToString(",")
                )
            )
            Log.d(TAG, "Сообщение успешно добавлено в chat_$chatId")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка вставки сообщения в chat_$chatId: ${e.message}")
        }
    }

    // Извлечение всех сообщений из таблицы чата
    fun getAllMessages(chatId: String): List<Messages_Chat> {
        Log.d(TAG, "Извлечение всех сообщений из chat_$chatId")
        val messages = mutableListOf<Messages_Chat>()
        val cursor = database.rawQuery("SELECT * FROM chat_$chatId", null)

        cursor.use {
            while (it.moveToNext()) {
                try {
                    val message = Messages_Chat(
                        messageId = it.getString(it.getColumnIndexOrThrow("message_id")),
                        content = it.getString(it.getColumnIndexOrThrow("content")),
                        profilerIMG = it.getString(it.getColumnIndexOrThrow("profilerIMG")),
                        messageTime = it.getLong(it.getColumnIndexOrThrow("messageTime")),
                        key = it.getString(it.getColumnIndexOrThrow("key")),
                        senderUsername = it.getString(it.getColumnIndexOrThrow("senderUsername")),
                        gifUrls = it.getString(it.getColumnIndexOrThrow("gifUrls")).split(",").filter { url -> url.isNotBlank() },
                        imageUrls = it.getString(it.getColumnIndexOrThrow("imageUrls")).split(",").filter { url -> url.isNotBlank() },
                        videoUrls = it.getString(it.getColumnIndexOrThrow("videoUrls")).split(",").filter { url -> url.isNotBlank() },
                        fileUrls = it.getString(it.getColumnIndexOrThrow("fileUrls")).split(",").filter { url -> url.isNotBlank() }
                    )
                    messages.add(message)
                } catch (e: Exception) {
                    Log.e(TAG, "Ошибка обработки сообщения из chat_$chatId: ${e.message}")
                }
            }
        }
        Log.d(TAG, "Извлечено ${messages.size} сообщений из chat_$chatId")
        return messages
    }

    suspend fun deleteMessageById(chatId: String, messageId: String) {
        withContext(Dispatchers.IO) {
            try {
                // Логируем начало операции удаления
                Log.i("ChatQueriesDB", "Начинается проверка наличия сообщения с ID $messageId в таблице chat_$chatId")

                val checkQuery = "SELECT * FROM chat_$chatId WHERE message_id = ?"
                val cursor = database.rawQuery(checkQuery, arrayOf(messageId))
                val recordExists = cursor.moveToFirst()

                // Логируем результат проверки наличия записи
                if (recordExists) {
                    Log.i("ChatQueriesDB", "Запись с ID $messageId найдена в таблице chat_$chatId")
                } else {
                    Log.e("ChatQueriesDB", "Запись с ID $messageId не найдена в таблице chat_$chatId")
                }
                cursor.close()

                if (recordExists) {
                    // Логируем начало транзакции для удаления записи
                    Log.i("ChatQueriesDB", "Начинается транзакция для удаления сообщения с ID $messageId из таблицы chat_$chatId")

                    database.beginTransaction()
                    try {
                        val deleteQuery = "DELETE FROM chat_$chatId WHERE message_id = ?"
                        val deletedRows = database.delete("chat_$chatId", "message_id = ?", arrayOf(messageId))

                        if (deletedRows > 0) {
                            Log.i("ChatQueriesDB", "Сообщение с ID $messageId успешно удалено из таблицы chat_$chatId")
                        } else {
                            Log.e("ChatQueriesDB", "Не удалось удалить сообщение с ID $messageId из таблицы chat_$chatId")
                        }

                        database.setTransactionSuccessful()
                    } finally {
                        database.endTransaction()
                        Log.i("ChatQueriesDB", "Транзакция завершена для удаления сообщения с ID $messageId из таблицы chat_$chatId")
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatQueriesDB", "Ошибка при удалении сообщения с ID $messageId из таблицы chat_$chatId", e)
            }

            // Логируем окончание операции удаления
            Log.i("ChatQueriesDB", "Операция удаления сообщения с ID $messageId из таблицы chat_$chatId завершена")
        }
    }


    // Удаление всех сообщений в чате
    fun deleteAllMessages(chatId: String) {
        val query = "DELETE FROM chat_$chatId"
        Log.d(TAG, "Удаление всех сообщений из chat_$chatId")
        try {
            database.execSQL(query)
            Log.d(TAG, "Все сообщения удалены из chat_$chatId")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка удаления всех сообщений из chat_$chatId: ${e.message}")
        }
    }
}

