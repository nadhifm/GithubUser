package com.nadhif.githubuser.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.nadhif.githubuser.db.AppDatabase
import com.nadhif.githubuser.db.UserDao

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val CODE_FAVORITE = 1
        private const val CODE_FAVORITE_ITEM = 2
        private const val TABLE_NAME = "users"
        private const val AUTHORITY = "com.nadhif.githubuser.provider"
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_NAME, CODE_FAVORITE)
            addURI(AUTHORITY, "$TABLE_NAME/#", CODE_FAVORITE_ITEM)
        }
    }

    private val userDao: UserDao by lazy {
        AppDatabase(requireNotNull(context)).getUserDao()
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException()
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException()
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException()
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? = context?.run {
        val cursor = when (uriMatcher.match(uri)) {
            CODE_FAVORITE -> userDao.cursorSelectAll()
            CODE_FAVORITE_ITEM -> userDao.cursorSelectById(ContentUris.parseId(uri))
            else -> null
        }
        cursor?.setNotificationUri(contentResolver, uri)
        cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw UnsupportedOperationException()
    }
}
