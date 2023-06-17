package com.example.tradeu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tradeu.data.entities.Favorite
import com.example.tradeu.data.entities.ItemSize
import com.example.tradeu.data.entities.User

@Dao
interface ItemSizeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListItemSizes(listItemSizes:List<ItemSize>)

    @Query("SELECT * FROM ukuran_pakaian")
    fun getListItemSizes():LiveData<List<ItemSize>>
}