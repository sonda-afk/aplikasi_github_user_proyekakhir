package com.sonda.bangkit.fundamentaltiga

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.AUTHOR
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.CONTENT_URI
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.TABLE_NAME
import com.sonda.bangkit.fundamentaltiga.database.FavoriteHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private lateinit var helper: FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHOR, TABLE_NAME, FAVORITE)
            sUriMatcher.addURI(
                    AUTHOR,
                    "$TABLE_NAME/#",
                    FAVORITE_ID
            )
        }
    }

    override fun onCreate(): Boolean {
        helper = FavoriteHelper.getInstance(context as Context)
        helper.openDatabase()
        return true
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun query(
            uri: Uri,
            strings: Array<String>?,
            s: String?,
            strings1: Array<String>?,
            s1: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> helper.queryAll()
            FAVORITE_ID -> helper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (FAVORITE) {
            sUriMatcher.match(uri) -> helper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
            uri: Uri,
            contentValues: ContentValues?,
            s: String?,
            strings: Array<String>?
    ): Int {
        val updated: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> helper.update(
                    uri.lastPathSegment.toString(),
                    contentValues
            )
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> helper.delete(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}