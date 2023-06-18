package com.example.tradeu.ui.mainpage.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeu.data.ItemRepository
import com.example.tradeu.data.UserRepository
import com.example.tradeu.data.entities.Item
import com.example.tradeu.helper.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UploadViewModel(
    private val itemRepository: ItemRepository,
    private val userRepository: UserRepository
):ViewModel() {
    private var userId = -1L

    private var _latestImageSelected = MutableLiveData<String?>()
    val latestImageSelected:LiveData<String?> = _latestImageSelected

    fun setImageSelected(uri:String?){
        _latestImageSelected.value = uri
    }

    private val _message = MutableLiveData<SingleEvent<String>>()
    val message: LiveData<SingleEvent<String>> = _message

    suspend fun addItem(
        imgUriString:String,
        productName:String,
        description:String,
        price:Long,
        stock:Short
    ) = withContext(Dispatchers.IO){
        if (userId!=-1L){
            itemRepository.insertItem(
                userId,
                productName,
                imgUriString,
                price,
                stock,
                description
            )
        }
    }

    private fun getUserId(){
        viewModelScope.launch(Dispatchers.IO) {
            userId = userRepository.getUserId()
        }
    }

    fun setSingleEventMessage(message:String){
        _message.value = SingleEvent(message)
    }


    init {
        getUserId()
    }
}