package com.nadhif.githubuser.ui.main.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nadhif.githubuser.db.AppDatabase
import com.nadhif.githubuser.network.response.User
import com.nadhif.githubuser.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoriteViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = UserRepository(AppDatabase(app))
    private var currentJob: Job? = null

    fun getAllFavoriteUsers() = repository.getAllFavoriteUsers()

    fun deleteSelectedUser(selectedUser: ArrayList<String>) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            repository.deleteSelectedUser(selectedUser)
        }
    }

    fun saveUser(user: User) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            repository.insert(user)
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }

    class Factory(
        val app: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavoriteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}