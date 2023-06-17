package com.example.tradeu.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeu.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository):ViewModel() {

    private val _isLoginSuccess = MutableLiveData<Boolean>()
    val isLoginSuccess:LiveData<Boolean> = _isLoginSuccess

    fun getListUsers()=userRepository.getListUser()

    fun login(username:String, password:String){
        viewModelScope.launch(Dispatchers.IO){
            val userId = userRepository.login(username, password)
            if (userId != null){
                userRepository.saveUserId(userId)
                _isLoginSuccess.postValue(true)
            }else{
                _isLoginSuccess.postValue(false)
            }

        }
    }
}