package com.nadhif.githubuser.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nadhif.githubuser.ui.followers.FollowersViewModel
import com.nadhif.githubuser.ui.following.FollowingViewModel

class ViewModelFactory(
    private val username: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FollowersViewModel::class.java) -> {
                FollowersViewModel(username) as T
            }
            modelClass.isAssignableFrom(FollowingViewModel::class.java) -> {
                FollowingViewModel(username) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}