package com.example.tradeu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tradeu.data.entities.Transaction
import com.example.tradeu.data.entities.relations.TransactionItemAndItemSize

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListTransaction(listTransaction:List<Transaction>)

    @Query("SELECT * FROM Transaksi")
    fun getListTransactions():LiveData<List<Transaction>>

    @androidx.room.Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transaction: Transaction):Long

    @Query("SELECT * FROM Transaksi WHERE no_transaksi = :transactionId")
    fun getTransactionById(transactionId:Long):LiveData<TransactionItemAndItemSize>

}