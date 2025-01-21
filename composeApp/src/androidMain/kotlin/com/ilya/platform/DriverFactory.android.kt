package com.ilya.platform

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.ilya.Database
actual class DriverFactory(private val context: Context) {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            Database.Schema,
            context,
            "bucket.db",
            callback = object : AndroidSqliteDriver.Callback(Database.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        )
    }

    fun createMessageTable(tableName: String): Boolean {
        val db = SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath("bucket.db"), null)
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS $tableName (
                message_id TEXT PRIMARY KEY,
                content TEXT,
                profilerIMG TEXT,
                messageTime INTEGER,
                key TEXT,
                senderUsername TEXT,
                gifUrls TEXT,
                imageUrls TEXT,
                videoUrls TEXT,
                fileUrls TEXT
            )
        """.trimIndent()
        return try {
            db.execSQL(createTableQuery)
            true
        } catch (e: Exception) {
            Log.e("DriverFactory", "Error creating table: $tableName", e)
            false
        }
    }
}

