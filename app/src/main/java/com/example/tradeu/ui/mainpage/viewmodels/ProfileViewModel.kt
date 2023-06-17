package com.example.tradeu.ui.mainpage.viewmodels

import androidx.lifecycle.*
import com.example.tradeu.data.UserRepository
import com.example.tradeu.helper.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository):ViewModel() {
    private val _isLogoutSuccess = MutableLiveData<SingleEvent<Boolean>>()
    val isLogoutSuccess:LiveData<SingleEvent<Boolean>> = _isLogoutSuccess

    private val userId = MutableLiveData<Long>()
    val userData = userId.switchMap {id ->
        userRepository.getUserData(id)
    }

    fun logout(){
        viewModelScope.launch (Dispatchers.IO){
            userRepository.deleteUserId()
            val currentUserId = userRepository.getUserId()
            if (currentUserId == -1L){
                _isLogoutSuccess.postValue(SingleEvent(true))
            }else _isLogoutSuccess.postValue(SingleEvent(false))
        }

    }

    private fun getUserId(){
        viewModelScope.launch (Dispatchers.IO){
            userId.postValue(userRepository.getUserId())
        }
    }

    init {
        getUserId()
    }
}