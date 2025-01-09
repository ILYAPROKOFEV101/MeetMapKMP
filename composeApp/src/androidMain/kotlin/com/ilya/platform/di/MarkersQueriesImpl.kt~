package com.ilya.platform.di

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log

class MarkersQueriesImpl(private val database: SQLiteDatabase) {

    fun selectAllMarkers(): List<Marker> {
        val markers = mutableListOf<Marker>()
        val cursor = database.rawQuery("SELECT * FROM markers", null)
        cursor.use {
            while (it.moveToNext()) {
                markers.add(
                    Marker(
                        id = it.getString(it.getColumnIndexOrThrow("id")),
                        key = it.getString(it.getColumnIndexOrThrow("key")),
                        username = it.getString(it.getColumnIndexOrThrow("username")),
                        imguser = it.getString(it.getColumnIndexOrThrow("imguser")),
                        photomark = it.getString(it.getColumnIndexOrThrow("photomark")),
                        street = it.getString(it.getColumnIndexOrThrow("street")),
                        lat = it.getDouble(it.getColumnIndexOrThrow("lat")),
                        lon = it.getDouble(it.getColumnIndexOrThrow("lon")),
                        name = it.getString(it.getColumnIndexOrThrow("name")),
                        whatHappens = it.getString(it.getColumnIndexOrThrow("whatHappens")),
                        startDate = it.getString(it.getColumnIndexOrThrow("startDate")),
                        endDate = it.getString(it.getColumnIndexOrThrow("endDate")),
                        startTime = it.getString(it.getColumnIndexOrThrow("startTime")),
                        endTime = it.getString(it.getColumnIndexOrThrow("endTime")),
                        participants = it.getInt(it.getColumnIndexOrThrow("participants")),
                        access = it.getInt(it.getColumnIndexOrThrow("access"))
                    )
                )
            }
        }
        return markers
    }

    fun getMarkerById(id: String): Marker? {
        val cursor = database.rawQuery("SELECT * FROM markers WHERE id = ?", arrayOf(id))
        cursor.use {
            if (it.moveToFirst()) {
                return Marker(
                    id = it.getString(it.getColumnIndexOrThrow("id")),
                    key = it.getString(it.getColumnIndexOrThrow("key")),
                    username = it.getString(it.getColumnIndexOrThrow("username")),
                    imguser = it.getString(it.getColumnIndexOrThrow("imguser")),
                    photomark = it.getString(it.getColumnIndexOrThrow("photomark")),
                    street = it.getString(it.getColumnIndexOrThrow("street")),
                    lat = it.getDouble(it.getColumnIndexOrThrow("lat")),
                    lon = it.getDouble(it.getColumnIndexOrThrow("lon")),
                    name = it.getString(it.getColumnIndexOrThrow("name")),
                    whatHappens = it.getString(it.getColumnIndexOrThrow("whatHappens")),
                    startDate = it.getString(it.getColumnIndexOrThrow("startDate")),
                    endDate = it.getString(it.getColumnIndexOrThrow("endDate")),
                    startTime = it.getString(it.getColumnIndexOrThrow("startTime")),
                    endTime = it.getString(it.getColumnIndexOrThrow("endTime")),
                    participants = it.getInt(it.getColumnIndexOrThrow("participants")),
                    access = it.getInt(it.getColumnIndexOrThrow("access"))
                )
            }
        }
        return null
    }

    fun deleteMarkerById(id: String) {
        try {
            database.execSQL("DELETE FROM markers WHERE id = ?", arrayOf(id))
            Log.d("MarkersQueriesImpl", "Marker with id $id deleted successfully")
        } catch (e: Exception) {
            Log.e("MarkersQueriesImpl", "Error deleting marker with id $id", e)
        }
    }


    fun getAllMarkers(): List<Marker> {
        val markers = mutableListOf<Marker>()
        val cursor = database.rawQuery("SELECT * FROM markers", null)
        cursor.use {
            while (it.moveToNext()) {
                markers.add(
                    Marker(
                        id = it.getString(it.getColumnIndexOrThrow("id")),
                        key = it.getString(it.getColumnIndexOrThrow("key")),
                        username = it.getString(it.getColumnIndexOrThrow("username")),
                        imguser = it.getString(it.getColumnIndexOrThrow("imguser")),
                        photomark = it.getString(it.getColumnIndexOrThrow("photomark")),
                        street = it.getString(it.getColumnIndexOrThrow("street")),
                        lat = it.getDouble(it.getColumnIndexOrThrow("lat")),
                        lon = it.getDouble(it.getColumnIndexOrThrow("lon")),
                        name = it.getString(it.getColumnIndexOrThrow("name")),
                        whatHappens = it.getString(it.getColumnIndexOrThrow("whatHappens")),
                        startDate = it.getString(it.getColumnIndexOrThrow("startDate")),
                        endDate = it.getString(it.getColumnIndexOrThrow("endDate")),
                        startTime = it.getString(it.getColumnIndexOrThrow("startTime")),
                        endTime = it.getString(it.getColumnIndexOrThrow("endTime")),
                        participants = it.getInt(it.getColumnIndexOrThrow("participants")),
                        access = it.getInt(it.getColumnIndexOrThrow("access"))
                    )
                )
            }
        }
        return markers
    }


    fun insertMarker(
        id: String,
        key: String,
        username: String,
        imguser: String,
        photomark: String,
        street: String,
        lat: Double,
        lon: Double,
        name: String,
        whatHappens: String,
        startDate: String,
        endDate: String,
        startTime: String,
        endTime: String,
        participants: Int,
        access: Int
    ) {
        // Проверяем, существует ли уже метка с таким ID
        val cursor = database.rawQuery("SELECT * FROM markers WHERE id = ?", arrayOf(id))
        if (cursor.moveToFirst()) {
            // Если метка уже существует, не вставляем её
            Log.d("MarkersQueries", "Marker with ID $id already exists. Skipping insert.")
            cursor.close()
            return
        }
        cursor.close()

        // Если метки нет, вставляем новую
        database.execSQL(
            """
        INSERT INTO markers (
            id, [key], username, imguser, photomark, street, lat, lon, name, whatHappens,
            startDate, endDate, startTime, endTime, participants, access
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent(),
            arrayOf(id, key, username, imguser, photomark, street, lat, lon, name, whatHappens, startDate, endDate, startTime, endTime, participants, access)
        )
    }

}
