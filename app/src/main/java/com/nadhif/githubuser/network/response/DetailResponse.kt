package com.nadhif.githubuser.network.response

import com.squareup.moshi.Json

data class DetailResponse(
    val id: Long,
    val login: String,
    val name: String?,
    val company: String?,
    @Json(name = "avatar_url")
    val avatarUrl: String,
    val location: String?,
    @Json(name = "public_repos")
    val repos: String,
    val followers: String,
    val following: String
)