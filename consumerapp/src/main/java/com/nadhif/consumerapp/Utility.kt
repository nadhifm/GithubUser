package com.nadhif.consumerapp

import android.net.Uri

object Utility {
    private const val TABLE_USER = "users"
    private const val AUTHORITY_USER = "com.nadhif.githubuser.provider"

    val CONTENT_USER_URI: Uri = Uri.Builder().scheme("content")
        .authority(AUTHORITY_USER)
        .appendPath(TABLE_USER)
        .build()

    const val COLUMN_LOGIN = "login"
    const val COLUMN_AVATAR_URL = "avatarUrl"

}