package com.example.tradeu.data


import androidx.lifecycle.LiveData
import com.example.tradeu.data.dao.FavoriteDao
import com.example.tradeu.data.dao.ItemDao
import com.example.tradeu.data.entities.Favorite
import com.example.tradeu.data.entities.Item
import com.example.tradeu.data.entities.relations.ItemAndFavorite
import com.example.tradeu.data.entities.relations.ItemAndFavoriteStatus
import com.example.tradeu.data.entities.relations.ItemUserAndFavorite
import com.example.tradeu.getCurrentDate

class ItemRepository private constructor(
    private val itemDao:ItemDao,
    private val favoriteDao: FavoriteDao
){

    fun getItems() = itemDao.getListItems()

    fun getListItems(userId: Long):LiveData<List<ItemAndFavoriteStatus>>{
        return itemDao.getListItemsWithFavoriteStatus(userId)
    }

    fun getItemByIdAndItsFavoriteStatus(userId: Long, itemId:Long):LiveData<ItemUserAndFavorite>{
        return itemDao.getItemByIdAndItsFavoriteStatus(userId, itemId)
    }

    fun getListNewsItems(userId: Long):LiveData<List<ItemAndFavoriteStatus>>{
        return itemDao.getNewsListItems(userId)
    }

    fun getListFavoritesItems(userId: Long):LiveData<List<ItemAndFavoriteStatus>>{
        return favoriteDao.getListTrendingItems(userId)
    }

    fun getListItemBySearch(userId: Long, searchQuery:String):LiveData<List<ItemAndFavoriteStatus>>{
        return itemDao.getListItemBySearch(userId, searchQuery)
    }

    fun getLisFavoriteItemsByUserId(userId:Long):LiveData<List<ItemAndFavorite>?>{
        return favoriteDao.getUserListFavoriteItems(userId)
    }

    suspend fun updateItemStock(itemId: Long){
        itemDao.updateItemStock(itemId)
    }

    suspend fun insertFavorite(favorite: Favorite){
        favoriteDao.insertFavorite(favorite)
    }

    suspend fun deleteFavorite(favorite: Favorite){
        favoriteDao.deleteFavorite(favorite)
    }


    suspend fun insertItem(
        sellerId:Long,
        productName:String,
        imgUriString:String,
        price:Long,
        stock:Short,
        description:String
    ){
        itemDao.insertItem(
            Item(
                sellerId = sellerId,
                itemName = productName,
                itemPhoto = imgUriString,
                price = price,
                stock = stock,
                publishDate = getCurrentDate(),
                description = description
            )
        )
    }

    companion object{
        @Volatile
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