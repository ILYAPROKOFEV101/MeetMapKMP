package com.ilya.platform.di

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_MARKERS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS markers")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "markers.db"
        private const val DATABASE_VERSION = 1

        private const val CREATE_TABLE_MARKERS = """
            CREATE TABLE markers (
                id TEXT PRIMARY KEY,
                [key] TEXT NOT NULL,
                username TEXT NOT NULL,
                imguser TEXT NOT NULL,
                photomark TEXT NOT NULL,
                street TEXT NOT NULL,
                lat REAL NOT NULL,
                lon REAL NOT NULL,
                name TEXT NOT NULL,
                whatHappens TEXT NOT NULL,
                startDate TEXT NOT NULL,
                endDate TEXT NOT NULL,
                startTime TEXT NOT NULL,
                endTime TEXT NOT NULL,
                participants INTEGER NOT NULL,
                access INTEGER NOT NULL
            )
        """
    }
}
