package com.nadhif.githubuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadhif.githubuser.network.response.User
import com.nadhif.githubuser.repository.UserRepository
import com.nadhif.githubuser.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel: ViewModel() {

    private val userRepository = UserRepository()
    private var currentJob: Job? = null

    private val _users = MutableLiveData<Resource<List<User>>>()
    val users: LiveData<Resource<List<User>>>
        get() = _users

    private fun getUsers() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _users.value = Resource.Loading
            val response = userRepository.getUsers()
            try {
                _users.value = Resource.Success(response)
            }catch (e: IOException) {
                _users.value = Resource.Error(e.message!!)
            }
        }
    }

    init {
        getUsers()
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }
}