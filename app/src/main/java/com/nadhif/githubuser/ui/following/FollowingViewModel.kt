package com.nadhif.githubuser.ui.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadhif.githubuser.network.GithubApi
import com.nadhif.githubuser.network.response.User
import com.nadhif.githubuser.repository.UserRepository
import com.nadhif.githubuser.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class FollowingViewModel(username: String) : ViewModel() {

    private val userRepository = UserRepository()
    private var currentJob: Job? = null

    private val _following = MutableLiveData<Resource<List<User>>>()
    val following: LiveData<Resource<List<User>>>
        get() = _following

    private fun getFollowing(username: String) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _following.value = Resource.Loading
            val response = userRepository.getFollowing(username)
            try {
                _following.value = Resource.Success(response)
            }catch (e: IOException) {
                _following.value = Resource.Error(e.message!!)
            }
        }
    }

    init {
        getFollowing(username)
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }
}