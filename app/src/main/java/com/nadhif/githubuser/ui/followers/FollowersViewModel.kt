package com.nadhif.githubuser.ui.followers

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

class FollowersViewModel(username: String) : ViewModel() {

    private val userRepository = UserRepository()
    private var currentJob: Job? = null

    private val _followers = MutableLiveData<Resource<List<User>>>()
    val followers: LiveData<Resource<List<User>>>
        get() = _followers

    private fun getFollowers(username: String) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _followers.value = Resource.Loading
            val response = userRepository.getFollowers(username)
            try {
                _followers.value = Resource.Success(response)
            }catch (e: IOException) {
                _followers.value = Resource.Error(e.message!!)
            }
        }
    }

    init {
        getFollowers(username)
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }
}