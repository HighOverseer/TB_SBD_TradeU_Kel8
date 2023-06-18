package com.example.tradeu.ui.mainpage.viewmodels

import androidx.lifecycle.*
import com.example.tradeu.data.ItemRepository
import com.example.tradeu.data.UserRepository
import com.example.tradeu.data.entities.Favorite
import com.example.tradeu.helper.SingleEvent
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


    private val _message = MutableLiveData<SingleEvent<String>>()
    val message: LiveData<SingleEvent<String>> = _message

    private fun getUserId(){
        viewModelScope.launch (Dispatchers.IO){
            _userId.postValue(userRepository.getUserId())
        }
    }

    fun getListFavoriteSearch(searchQ:String?){
        searchQuery = searchQ
        _userId.value = _userId.value
    }

    fun setSingleEventMessage(message:String){
        _message.value = SingleEvent(message)
    }

    fun deleteFavorite(itemId:Long){
        viewModelScope.launch(Dispatchers.IO) {
            _userId.value?.let {
                itemRepository.deleteFavorite(Favorite(it, itemId))
            }
        }
    }

    init {
        getUserId()
    }
}