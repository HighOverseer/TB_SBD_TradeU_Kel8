package com.example.tradeu.ui.productdetail

import androidx.lifecycle.*
import com.example.tradeu.data.ItemRepository
import com.example.tradeu.data.UserRepository
import com.example.tradeu.data.entities.Favorite
import com.example.tradeu.helper.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val userRepository: UserRepository,
    private val itemRepository: ItemRepository
):ViewModel() {

    private val _userId = MutableLiveData<Long>()
    val userId:LiveData<Long> = _userId

    private val _latestSelectedItemSize = MutableLiveData<ItemSizeEnum>()
    val latestSelectedItemSize:LiveData<ItemSizeEnum> = _latestSelectedItemSize


    val productData = _userId.switchMap { userId ->
        itemRepository.getItemByIdAndItsFavoriteStatus(userId, itemId)
    }

    private var itemId:Long = -1L

    private val _message = MutableLiveData<SingleEvent<String>>()
    val message:LiveData<SingleEvent<String>> = _message

    fun addFavorite(){
        viewModelScope.launch(Dispatchers.IO){
            _userId.value?.let {
                itemRepository.insertFavorite(Favorite(it, itemId))
            }
        }
    }

    fun deleteFavorite(){
        viewModelScope.launch(Dispatchers.IO) {
            _userId.value?.let {
                itemRepository.deleteFavorite(Favorite(it, itemId))
            }
        }
    }

     fun initData(itemId:Long){
         if (itemId != -1L){
             this.itemId = itemId
             viewModelScope.launch (Dispatchers.IO){
                 _userId.postValue(userRepository.getUserId())
             }
         }else return

    }

    fun setMessage(message:String){
        _message.value = SingleEvent(message)
    }

    fun setLatestItemSize(itemSizeEnum: ItemSizeEnum){
        _latestSelectedItemSize.value = itemSizeEnum
    }

}