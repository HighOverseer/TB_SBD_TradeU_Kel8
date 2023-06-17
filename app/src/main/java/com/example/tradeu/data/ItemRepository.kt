package com.example.tradeu.data


import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.tradeu.data.dao.FavoriteDao
import com.example.tradeu.data.dao.ItemDao
import com.example.tradeu.data.entities.Item
import com.example.tradeu.data.entities.relations.ItemAndFavorite
import com.example.tradeu.data.entities.relations.ItemUserAndFavorite

class ItemRepository private constructor(
    private val itemDao:ItemDao,
    private val favoriteDao: FavoriteDao
){

    fun getListItems():LiveData<List<Item>>{
        return itemDao.getListItems()
    }

    fun getListNewsItems():LiveData<List<Item>>{
        return itemDao.getNewsListItems()
    }

    fun getListFavoritesItems():LiveData<List<ItemUserAndFavorite>>{
        return favoriteDao.getListFavoriteItems()
    }

    fun getListItemBySearch(searchQuery:String):LiveData<List<Item>>{
        return itemDao.getListItemBySearch(searchQuery)
    }

    fun getLisFavoriteItemsByUserId(userId:Long):LiveData<List<ItemAndFavorite>>{
        return favoriteDao.getListFavoriteItemsByUserId(userId)
    }


    companion object{
        private var INSTANCE:ItemRepository?=null

        fun getInstance(
            itemDao: ItemDao,
            favoriteDao: FavoriteDao
        ):ItemRepository{
            return INSTANCE?: synchronized(this){
                INSTANCE?:ItemRepository(itemDao, favoriteDao)
            }.also { INSTANCE = it }
        }
    }
}