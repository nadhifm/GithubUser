package com.nadhif.githubuser.network.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String
)
