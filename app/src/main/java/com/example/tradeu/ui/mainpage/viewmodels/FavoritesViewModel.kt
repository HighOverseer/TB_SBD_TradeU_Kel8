package com.example.tradeu.ui.mainpage.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.tradeu.data.ItemRepository
import com.example.tradeu.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val userRepository: UserRepository,
    private val itemRepository: ItemRepository
):ViewModel() {
    private val _userId = MutableLiveData<Long>()
    val listFavoriteItems = _userId.switchMap { userId ->
        itemRepository.getLisFavoriteItemsByUserId(userId)
    }

    var searchQuery:String?=null
        private set

    private fun getUserId(){
        viewModelScope.launch (Dispatchers.IO){
            _userId.postValue(userRepository.getUserId())
        }
    }

    fun getListFavoriteSearch(searchQ:String?){
        searchQuery = searchQ
        _userId.value = _userId.value
    }

    init {
        getUserId()
    }
}