package com.example.tradeu.data

import androidx.lifecycle.LiveData
import com.example.tradeu.data.dao.ItemDao
import com.example.tradeu.data.entities.Transaction
import com.example.tradeu.data.dao.TransactionDao
import com.example.tradeu.data.dao.UserDao
import com.example.tradeu.data.entities.relations.TransactionItemAndItemSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRepository private constructor(
    private val transactionDao: TransactionDao,
    private val userDao: UserDao,
    private val itemDao: ItemDao
){
    @androidx.room.Transaction
    suspend fun insertTransaction(
        userId:Long,
        buyerName:String,
        buyerAddress:String,
        buyerContact:String,
        quantity:Short,
        itemId:Long,
        currentItemPrice:Long,
        selectedSizeId:Long

    ):Long = withContext(Dispatchers.IO){
        val totalPayment = currentItemPrice * quantity + 10_000
        userDao.updateUserWallet(userId, totalPayment)
        itemDao.updateItemStock(itemId)
        transactionDao.insertTransaction(
            Transaction(
                buyerId = userId,
                buyerName = buyerName,
                buyerAddress = buyerAddress,
                buyerContact = buyerContact,
                quantity = quantity,
                itemId = itemId,
                itemPrice = currentItemPrice,
                total = totalPayment,
                sizeId = selectedSizeId
            )
        )
    }

    fun getTransactionById(transactionId:Long):LiveData<TransactionItemAndItemSize>{
        return transactionDao.getTransactionById(transactionId)
    }

    companion object{
        @Volatile
        private var INSTANCE:TransactionRepository?=null

        fun getInstance(
            transactionDao: TransactionDao,
            userDao: UserDao,
            itemDao: ItemDao
        ):TransactionRepository{
            return INSTANCE?: synchronized(this){
                INSTANCE?:TransactionRepository(transactionDao, userDao, itemDao)
            }.also { INSTANCE = it }
        }
    }
}