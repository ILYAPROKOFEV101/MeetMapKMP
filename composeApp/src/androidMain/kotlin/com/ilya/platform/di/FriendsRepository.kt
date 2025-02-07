package com.ilya.platform.di

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.ilya.MeetingMap.SocialMap.DataModel.FindFriends
import com.ilya.meetmapkmp.SocialMap.DataModel.Friend

class FriendsRepository(private val database: SQLiteDatabase) {

    companion object {
        private const val TABLE_NAME = "friends"
    }

    // Интерфейс для уведомления об изменениях
    var onDatabaseChanged: (() -> Unit)? = null


    // Получение друга по token
    fun getFriendByToken(token: String): Friend? {
        val cursor = database.rawQuery("SELECT * FROM $TABLE_NAME WHERE token = ?", arrayOf(token))
        cursor.use {
            if (it.moveToFirst()) {
                return Friend(
                    token = it.getString(it.getColumnIndexOrThrow("token")),
                    key = it.getString(it.getColumnIndexOrThrow("key")),
                    img = it.getString(it.getColumnIndexOrThrow("img")),
                    lastmessage = it.getString(it.getColumnIndexOrThrow("lastmessage")),
                    name = it.getString(it.getColumnIndexOrThrow("name")),
                    online = it.getInt(it.getColumnIndexOrThrow("online")) == 1
                )
            }
        }
        return null
    }

    // Удаление друга по token
    fun deleteFriendByToken(token: String) {
        try {
            database.execSQL("DELETE FROM $TABLE_NAME WHERE token = ?", arrayOf(token))
            notifyDatabaseChanged()
            Log.d("FriendsRepository", "Friend with token $token deleted successfully.")
        } catch (e: Exception) {
            Log.e("FriendsRepository", "Error deleting friend with token $token: ${e.message}")
        }
    }

    // Удаление всех друзей из таблицы
    fun deleteAllFriends() {
        try {
            database.execSQL("DELETE FROM $TABLE_NAME")
            notifyDatabaseChanged()
            Log.d("FriendsRepository", "All friends deleted successfully.")
        } catch (e: Exception) {
            Log.e("FriendsRepository", "Error deleting all friends: ${e.message}")
        }
    }

    // Метод для уведомления об изменениях
    private fun notifyDatabaseChanged() {
        onDatabaseChanged?.invoke()
    }

    fun insertOrUpdateFriend(friend: Friend) {
        try {
            val cursor = database.rawQuery("SELECT * FROM $TABLE_NAME WHERE token = ?", arrayOf(friend.token))
            if (cursor.count > 0) {
                cursor.close()
                updateFriend(friend)
            } else {
                cursor.close()
                insertFriend(friend)
            }
            notifyDatabaseChanged()
        } catch (e: Exception) {
            Log.e("FriendsRepository", "Error inserting/updating friend: ${e.message}")
        }
    }

    private fun insertFriend(friend: Friend) {
        database.execSQL(
            """
        INSERT INTO $TABLE_NAME (
            token, `key`, img, lastmessage, name, online
        ) VALUES (?, ?, ?, ?, ?, ?)
        """.trimIndent(),
            arrayOf(
                friend.token,
                friend.key,
                friend.img,
                friend.lastmessage,
                friend.name,
                if (friend.online) 1 else 0
            )
        )
    }

    private fun updateFriend(friend: Friend) {
        database.execSQL(
            """
        UPDATE $TABLE_NAME 
        SET `key` = ?, img = ?, lastmessage = ?, name = ?, online = ?
        WHERE token = ?
        """.trimIndent(),
            arrayOf(
                friend.key,
                friend.img,
                friend.lastmessage,
                friend.name,
                if (friend.online) 1 else 0,
                friend.token
            )
        )
    }

    fun getAllFriends(): List<Friend> {
        val friendsList = mutableListOf<Friend>()
        val cursor = database.query("friends", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val token = cursor.getString(cursor.getColumnIndexOrThrow("token"))
            val img = cursor.getString(cursor.getColumnIndexOrThrow("img"))
            val lastmessage = cursor.getString(cursor.getColumnIndexOrThrow("lastmessage"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val online = cursor.getInt(cursor.getColumnIndexOrThrow("online")) == 1
            val key = cursor.getString(cursor.getColumnIndexOrThrow("key")) // Экранируем зарезервированное слово

            friendsList.add(
                Friend(
                    token = token,
                    img = img,
                    lastmessage = lastmessage,
                    name = name,
                    online = online,
                    key = key
                )
            )
        }
        cursor.close()
        return friendsList
    }
}
