package com.nadhif.githubuser.utils

sealed class Resource<out T: Any> {
    object Loading : Resource<Nothing>()
    data class Success<out T: Any>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}