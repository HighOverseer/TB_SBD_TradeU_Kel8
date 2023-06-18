package com.example.tradeu.ui.mainpage.viewmodels

import androidx.lifecycle.*
import com.example.tradeu.data.ItemRepository
import com.example.tradeu.data.UserRepository
import com.example.tradeu.data.entities.Favorite
import com.example.tradeu.data.entities.Item
import com.example.tradeu.data.entities.relations.ItemAndFavoriteStatus
import com.example.tradeu.helper.SingleEvent
import com.example.tradeu.ui.mainpage.domain.HomeTab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val itemRepository: ItemRepository,
):ViewModel() {

    private val _selectedTab = MediatorLiveData<HomeTab?>()

    private var searchQuery:String?=null

    private val userId = MutableLiveData<Long>()
    val userData = userId.switchMap {id ->
        userRepository.getUserData(id)
    }

    private val _message = MutableLiveData<SingleEvent<String>>()
    val message:LiveData<SingleEvent<String>> = _message

    val listItem:LiveData<List<ItemAndFavoriteStatus>> = _selectedTab.switchMap { homeTab ->
        when(homeTab){
            HomeTab.All -> {
                searchQuery = null
                userId.value?.let {
                    itemRepository.getListItems(it)
                }
            }
            HomeTab.New -> {
                searchQuery = null
                userId.value?.let {
                    itemRepository.getListNewsItems(it)
                }
            }
            HomeTab.TrendingNow -> {
                searchQuery = null
                userId.value?.let {
                    itemRepository.getListFavoritesItems(it)
                }
            }
            null -> searchQuery?.let { searchQ -> userId.value?.let { itemRepository.getListItemBySearch(it, searchQ) } }
        }
    }

    fun setCurrentTab(homeTab: HomeTab?){
        _selectedTab.value = homeTab
    }

    fun getCurrentTab() = _selectedTab.value

    fun getListItemSearch(searchQ: String){
        searchQuery = searchQ
        setCurrentTab(null)
    }

    private fun getUserId(){
        viewModelScope.launch (Dispatchers.IO){
            userId.postValue(userRepository.getUserId())
            _selectedTab.postValue(HomeTab.All)
        }
    }

    fun addFavorite(itemId:Long){
        viewModelScope.launch(Dispatchers.IO){
            userId.value?.let {
                itemRepository.insertFavorite(Favorite(it, itemId))
            }
        }
    }

    fun deleteFavorite(itemId:Long){
        viewModelScope.launch(Dispatchers.IO) {
            userId.value?.let {
                itemRepository.deleteFavorite(Favorite(it, itemId))
            }
        }
    }

    fun setSingleEventMessage(message:String){
        _message.value = SingleEvent(message)
    }

    init {
        getUserId()

        _selectedTab.addSource(userId){id ->
            id?.let { _selectedTab.value = _selectedTab.value }
        }
    }
}