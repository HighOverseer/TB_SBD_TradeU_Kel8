package com.example.tradeu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tradeu.data.entities.Favorite
import com.example.tradeu.data.entities.User
import com.example.tradeu.data.entities.relations.ItemAndFavorite
import com.example.tradeu.data.entities.relations.ItemUserAndFavorite
import java.util.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListFavorites(listFavorites:List<Favorite>)

    @Query("SELECT * FROM favorite")
    fun getListFavorites():LiveData<List<Favorite>>

    @Transaction
    @Query("SELECT * FROM Barang")
    fun getListFavoriteItems():LiveData<List<ItemUserAndFavorite>>

    @Transaction
    @Query("SELECT * FROM Favorite WHERE id_user = :userId")
    fun getListFavoriteItemsByUserId(userId:Long):LiveData<List<ItemAndFavorite>>

}