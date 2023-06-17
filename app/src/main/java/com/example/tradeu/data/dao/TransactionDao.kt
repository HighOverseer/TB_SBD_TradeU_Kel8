package com.example.tradeu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tradeu.data.entities.Favorite
import com.example.tradeu.data.entities.Item
import com.example.tradeu.data.entities.Transaction

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListTransaction(listTransaction:List<Transaction>)

    @Query("SELECT * FROM transaksi")
    fun getListTransactions():LiveData<List<Transaction>>
}