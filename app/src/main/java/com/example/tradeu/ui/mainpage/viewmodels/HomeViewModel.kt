package com.example.tradeu.ui.mainpage.viewmodels

import androidx.lifecycle.*
import com.example.tradeu.data.ItemRepository
import com.example.tradeu.data.UserRepository
import com.example.tradeu.ui.mainpage.domain.HomeTab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val itemRepository: ItemRepository,
):ViewModel() {
    private val _selectedTab = MutableLiveData(HomeTab.All)

    private var searchQuery:String?=null

    private val userId = MutableLiveData<Long>()
    val userData = userId.switchMap {id ->
        userRepository.getUserData(id)
    }

    val listItem = _selectedTab.switchMap { homeTab ->
        when(homeTab){
            HomeTab.All -> {
                searchQuery = null
                itemRepository.getListItems()
            }
            HomeTab.New -> {
                searchQuery = null
                itemRepository.getListNewsItems()
            }
            HomeTab.TrendingNow -> {
                searchQuery = null
                itemRepository.getListFavoritesItems()
            }
            null -> searchQuery?.let { itemRepository.getListItemBySearch(it) }
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
        }
    }

    init {
        getUserId()
    }
}