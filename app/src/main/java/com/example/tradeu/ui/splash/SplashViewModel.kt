package com.example.tradeu.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeu.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel(private val userRepository: UserRepository):ViewModel(){

    var userId:Long = -1L
        private set

    private fun getUserId(){
        viewModelScope.launch(Dispatchers.IO) {
            userId = userRepository.getUserId()
        }
    }

    init {
        getUserId()
    }
}