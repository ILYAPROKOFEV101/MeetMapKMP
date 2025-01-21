package com.ilya.platform.di

import android.database.sqlite.SQLiteDatabase
import com.ilya.meetmapkmp.SocialMap.DataModel.Messages_Chat

class ChatQueriesImpl(private val database: SQLiteDatabase) {

    // Добавление сообщения в таблицу
    fun insertMessage(chatId: String, message: Messages_Chat) {
        val query = """
            INSERT INTO chat_$chatId (
                message_id, content, profilerIMG, messageTime, key, senderUsername, gifUrls, imageUrls, videoUrls, fileUrls
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

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
    }

    // Извлечение всех сообщений из таблицы чата
    fun getAllMessages(chatId: String): List<Messages_Chat> {
        val messages = mutableListOf<Messages_Chat>()
        val cursor = database.rawQuery("SELECT * FROM chat_$chatId", null)

        cursor.use {
            while (it.moveToNext()) {
                messages.add(
                    Messages_Chat(
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
                )
            }
        }
        return messages
    }

    // Удаление сообщения по ID
    fun deleteMessageById(chatId: String, messageId: String) {
        val query = "DELETE FROM chat_$chatId WHERE message_id = ?"
        database.execSQL(query, arrayOf(messageId))
    }

    // Удаление всех сообщений в чате
    fun deleteAllMessages(chatId: String) {
        val query = "DELETE FROM chat_$chatId"
        database.execSQL(query)
    }
}

