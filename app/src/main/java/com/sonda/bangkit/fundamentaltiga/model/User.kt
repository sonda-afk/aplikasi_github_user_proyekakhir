package com.sonda.bangkit.fundamentaltiga.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
        var avatar: String? = null,
        var username: String? = null,
        var name: String? = null,
        var location: String? = null,
        var repository: String? = null,
        var company: String? = null,
        var following: String? = null,
        var followers: String? = null
) : Parcelable
