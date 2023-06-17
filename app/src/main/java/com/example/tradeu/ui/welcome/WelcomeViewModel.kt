package com.example.tradeu.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeu.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val userRepository: UserRepository
):ViewModel() {
    private val _isAlreadyLoggedIn = MutableLiveData<Boolean>()
    val isAlreadyLoggedIn:LiveData<Boolean> = _isAlreadyLoggedIn

    private fun checkLoginStatus(){
        viewModelScope.launch(Dispatchers.IO){
            val userId = userRepository.getUserId()
            if (userId != -1L){
                _isAlreadyLoggedIn.postValue(true)
            }else _isAlreadyLoggedIn.postValue(false)
        }
    }

    init {
        checkLoginStatus()
    }
}