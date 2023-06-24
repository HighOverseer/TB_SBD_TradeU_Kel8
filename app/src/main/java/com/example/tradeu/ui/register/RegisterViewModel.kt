package com.example.tradeu.ui.register

import androidx.lifecycle.*
import com.example.tradeu.data.UserRepository
import com.example.tradeu.helper.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(
    private val userRepository: UserRepository
): ViewModel() {


    private val _isRegisterSuccess = MutableLiveData<Boolean>()
    val isRegisterSuccess:LiveData<Boolean> = _isRegisterSuccess

    val message:LiveData<SingleEvent<String>> = _isRegisterSuccess.map {isRegisterSuccess ->
        if (isRegisterSuccess){
            SingleEvent("Register Success")
        }else SingleEvent("Register Failed")
    }

    fun register(username:String, password:String, fullname:String, address:String){
        viewModelScope.launch(Dispatchers.IO) {
            val isRegisterSuccess = userRepository.register(
                username, password, fullname, address
            )
            _isRegisterSuccess.postValue(isRegisterSuccess)
        }
    }


}