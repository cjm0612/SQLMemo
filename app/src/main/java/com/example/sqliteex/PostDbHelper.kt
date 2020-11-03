package com.example.sqliteex

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase

class PostDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createSql = CREATE_SQL_VER3
        db?.execSQL(createSql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        when(oldVersion){
            1 -> db?.execSQL("ALTER TABLE post ADD COLUMN post TEXT")
            in 1..2 -> db?.execSQL("ALTER TABLE post ADD COLUMN time TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        }
    }

    companion object{
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "post.db"

        const val CREATE_SQL_VER1 = "CREATE TABLE post (" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT )"

        const val CREATE_SQL_VER2 = "CREATE TABLE post (" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT, " +
                "post TEXT )"

        const val CREATE_SQL_VER3 = "CREATE TABLE post (" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT," +
                "post TEXT, " +
                "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP )"
    }
}