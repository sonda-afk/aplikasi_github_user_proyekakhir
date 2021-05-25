package com.sonda.bangkit.fundamentaltiga.database

import android.net.Uri
import android.provider.BaseColumns

object Database {
    class Columns : BaseColumns {
        companion object {
            const val AUTHOR = "com.sonda.bangkit.fundamentaltiga"
            const val TABLE_NAME = "favorite"
            const val IMAGE = "avatar"
            const val USERNAME = "username"
            const val NAME = "name"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val COMPANY = "company"
            const val FOLLOWING = "following"
            const val FOLLOWERS = "followers"
            const val FAVORITE = "checkFav"

            val CONTENT_URI = Uri.Builder()
                    .appendPath(TABLE_NAME)
                    .scheme("content")
                    .authority(AUTHOR)
                    .build()
        }
    }
}