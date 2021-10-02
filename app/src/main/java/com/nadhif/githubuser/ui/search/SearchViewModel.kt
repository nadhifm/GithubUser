package com.nadhif.githubuser.ui.search

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

class SearchViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private var currentJob: Job? = null

    private val _searchUser = MutableLiveData<Resource<List<User>>>()
    val searchUser: LiveData<Resource<List<User>>>
        get() = _searchUser

    fun searchUser(query: String) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _searchUser.value = Resource.Loading
            val response = userRepository.searchUser(query)
            try {
                _searchUser.value = Resource.Success(response.items)
            }catch (e: IOException) {
                _searchUser.value = Resource.Error(e.message!!)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }
}