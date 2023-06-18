package com.example.tradeu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tradeu.data.entities.Item
import com.example.tradeu.data.entities.relations.ItemAndFavoriteStatus
import com.example.tradeu.data.entities.relations.ItemUserAndFavorite


@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListItems(listItem:List<Item>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: Item)

    @Query("SELECT * FROM barang")
    fun getListItems():LiveData<List<Item>>
    @Transaction
    @Query("SELECT * FROM Barang LEFT JOIN Favorite ON Barang.id_barang = Favorite.id_barangg AND :userId = Favorite.id_user")
    fun getListItemsWithFavoriteStatus(userId:Long):LiveData<List<ItemAndFavoriteStatus>>
    @Transaction
    @Query("SELECT * FROM Barang LEFT JOIN Favorite ON Barang.id_barang = Favorite.id_barangg AND :userId = Favorite.id_user ORDER BY Barang.tgl_publish DESC")
    fun getNewsListItems(userId: Long):LiveData<List<ItemAndFavoriteStatus>>

    @Transaction
    @Query("SELECT * FROM Barang LEFT JOIN Favorite ON Barang.id_barang = Favorite.id_barangg AND :userId = Favorite.id_user WHERE nama_barang LIKE '%' || :searchQuery || '%'")
    fun getListItemBySearch(userId: Long, searchQuery:String):LiveData<List<ItemAndFavoriteStatus>>
    @Transaction
    @Query("SELECT Barang.*, User.alamat, (SELECT EXISTS(SELECT * FROM Favorite WHERE Favorite.id_user = :userId AND Favorite.id_barangg = :itemId)) as status_favorite FROM Barang INNER JOIN User ON Barang.id_penjual = User.id_user WHERE Barang.id_barang = :itemId")
    fun getItemByIdAndItsFavoriteStatus(userId:Long, itemId:Long):LiveData<ItemUserAndFavorite>

    @Transaction
    @Query("UPDATE Barang SET stok = ((SELECT stok FROM Barang WHERE id_barang = :itemId) - 1) WHERE id_barang = :itemId")
    suspend fun updateItemStock(itemId: Long)

}