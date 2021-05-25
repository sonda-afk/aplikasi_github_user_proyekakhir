package com.sonda.bangkit.fundamentaltiga

import android.database.Cursor
import com.sonda.bangkit.fundamentaltiga.database.Database
import com.sonda.bangkit.fundamentaltiga.model.Favorite

object MappingHelper {

    fun mapCursor(cursor: Cursor?): ArrayList<Favorite> {
        val favoriteList = ArrayList<Favorite>()

        cursor?.apply {
            while (moveToNext()) {
                val avatar = getString(getColumnIndexOrThrow(Database.Columns.IMAGE))
                val username = getString(getColumnIndexOrThrow(Database.Columns.USERNAME))
                val name = getString(getColumnIndexOrThrow(Database.Columns.NAME))
                val location = getString(getColumnIndexOrThrow(Database.Columns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(Database.Columns.REPOSITORY))
                val company = getString(getColumnIndexOrThrow(Database.Columns.COMPANY))
                val following = getString(getColumnIndexOrThrow(Database.Columns.FOLLOWING))
                val followers = getString(getColumnIndexOrThrow(Database.Columns.FOLLOWERS))
                val favorite =
                        getString(getColumnIndexOrThrow(Database.Columns.FAVORITE))
                favoriteList.add(
                        Favorite(
                                avatar,
                                username,
                                name,
                                location,
                                repository,
                                company,
                                following,
                                followers,
                                favorite
                        )
                )
            }
        }
        return favoriteList
    }
}