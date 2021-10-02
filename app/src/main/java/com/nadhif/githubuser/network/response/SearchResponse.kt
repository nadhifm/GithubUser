package com.nadhif.githubuser.network.response

data class SearchResponse(
    val total_count: Int = 0,
    val incomplete_results: Boolean,
    val items: List<User>
)