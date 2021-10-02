package com.nadhif.githubuser.ui.detail

import android.app.Application
import androidx.lifecycle.*
import com.nadhif.githubuser.db.AppDatabase
import com.nadhif.githubuser.network.response.DetailResponse
import com.nadhif.githubuser.network.response.User
import com.nadhif.githubuser.repository.UserRepository
import com.nadhif.githubuser.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class DetailViewModel(
    application: Application,
    username: String
) : AndroidViewModel(application) {

    private val userRepository = UserRepository(AppDatabase(application))
    private var currentJob: Job? = null

    private val _detailUser = MutableLiveData<Resource<DetailResponse>>()
    val detailUser: LiveData<Resource<DetailResponse>>
        get() = _detailUser

    private fun getDetailUser(username: String) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _detailUser.value = Resource.Loading
            val response = userRepository.getUserDetail(username)
            try {
                _detailUser.value = Resource.Success(response)
            }catch (e: IOException) {
                _detailUser.value = Resource.Error(e.message!!)
            }

        }
    }

    init {
        getDetailUser(username)
    }

    fun saveUser(user: User) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            userRepository.insert(user)
        }
    }

    fun checkUser(username: String) = userRepository.isFavorite(username)

    fun deleteUser(user: User) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            userRepository.deleteUser(user)
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }

    class Factory(
        val app: Application,
        val username: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(app, username) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

