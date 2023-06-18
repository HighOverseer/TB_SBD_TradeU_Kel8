package com.example.tradeu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tradeu.data.entities.Favorite
import com.example.tradeu.data.entities.relations.ItemAndFavorite
import com.example.tradeu.data.entities.relations.ItemAndFavoriteStatus
import java.util.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListFavorites(listFavorites:List<Favorite>)

    @Query("SELECT * FROM favorite")
    fun getListFavorites():LiveData<List<Favorite>>

    @Transaction
    @Query("SELECT * FROM (SELECT Barang.*,COUNT(Favorite.id_barangg) as total_count FROM Barang LEFT JOIN Favorite ON Favorite.id_barangg = Barang.id_barang GROUP BY Barang.id_barang ORDER BY total_count DESC) as SortedBarang LEFT JOIN Favorite ON SortedBarang.id_barang = Favorite.id_barangg AND :userId = Favorite.id_user")
    fun getListTrendingItems(userId:Long):LiveData<List<ItemAndFavoriteStatus>>

    @Transaction
    @Query("SELECT * FROM Favorite WHERE id_user = :userId")
    fun getUserListFavoriteItems(userId:Long):LiveData<List<ItemAndFavorite>?>


}