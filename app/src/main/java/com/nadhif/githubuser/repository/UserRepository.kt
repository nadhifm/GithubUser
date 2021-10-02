package com.nadhif.githubuser.repository

import com.nadhif.githubuser.db.AppDatabase
import com.nadhif.githubuser.network.GithubApi
import com.nadhif.githubuser.network.response.User

class UserRepository(
    private val appDatabase: AppDatabase? = null
) {
    suspend fun getUsers() = GithubApi.retrofitService.getUsersAsync().await()
    suspend fun searchUser(query: String) = GithubApi.retrofitService.searchUserAsync(query).await()
    suspend fun getUserDetail(username: String) = GithubApi.retrofitService.getUserDetailAsync(username).await()
    suspend fun getFollowers(username: String) = GithubApi.retrofitService.getFollowersAsync(username).await()
    suspend fun getFollowing(username: String) = GithubApi.retrofitService.getFollowingAsync(username).await()

    suspend fun insert(user: User) = appDatabase?.getUserDao()?.insert(user)
    fun isFavorite(username: String) = appDatabase?.getUserDao()?.isFavorite(username)
    fun getAllFavoriteUsers() = appDatabase?.getUserDao()?.getAllFavoriteUsers()
    suspend fun deleteUser(user: User) = appDatabase?.getUserDao()?.deleteUser(user)
    suspend fun deleteSelectedUser(selectedUser: ArrayList<String>) = appDatabase?.getUserDao()?.deleteSelectedUser(selectedUser)
}