package com.sonda.bangkit.fundamentaltiga.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.USERNAME

class FavoriteHelper(context: Context) : SQLiteOpenHelper(
        context,
        DATABASE_NAME, null,
        DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "favoriteDB"
        private const val DATABASE_TABLE = Database.Columns.TABLE_NAME
        private const val DATABASE_VERSION = 1

        private var INSTANCE: FavoriteHelper? = null
        fun getInstance(context: Context): FavoriteHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavoriteHelper(context)
        }
    }


    private var database: SQLiteDatabase = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE ${Database.Columns.TABLE_NAME}" +
                " ($USERNAME TEXT PRIMARY KEY  NOT NULL," +
                " ${Database.Columns.IMAGE} TEXT NOT NULL," +
                " ${Database.Columns.NAME} TEXT NOT NULL," +
                " ${Database.Columns.LOCATION} TEXT NOT NULL," +
                " ${Database.Columns.REPOSITORY} TEXT NOT NULL," +
                " ${Database.Columns.COMPANY} TEXT NOT NULL," +
                " ${Database.Columns.FOLLOWING} TEXT NOT NULL," +
                " ${Database.Columns.FOLLOWERS} TEXT NOT NULL," +
                " ${Database.Columns.FAVORITE} TEXT NOT NULL)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${Database.Columns.TABLE_NAME}")
        onCreate(db)
    }

    @Throws(SQLException::class)
    fun openDatabase() {
        database = this.writableDatabase
    }

    fun closeDatabase() {
        this.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$USERNAME ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                "$USERNAME = ?",
                arrayOf(id),
                null,
                null,
                null,
                null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$USERNAME = ?", arrayOf(id))
    }

    fun delete(id: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$id'", null)
    }

}