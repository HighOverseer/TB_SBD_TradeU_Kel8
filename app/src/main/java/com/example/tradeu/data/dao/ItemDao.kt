package com.example.tradeu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tradeu.data.entities.Item


@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListItems(listItem:List<Item>)

    @Query("SELECT * FROM barang")
    fun getListItems():LiveData<List<Item>>

    @Query("SELECT * FROM barang ORDER BY tgl_publish DESC")
    fun getNewsListItems():LiveData<List<Item>>

    @Query("SELECT * FROM barang WHERE nama_barang LIKE '%' || :searchQuery || '%'")
    fun getListItemBySearch(searchQuery:String):LiveData<List<Item>>

}