package com.example.tradeu

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.tradeu.data.ItemRepository
import com.example.tradeu.data.TransactionRepository
import com.example.tradeu.data.UserRepository
import com.example.tradeu.data.dao.ItemDao
import com.example.tradeu.data.database.TradeUDatabase
import com.example.tradeu.data.preference.UserAuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class MyApplication:Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { TradeUDatabase.getDatabase(this, applicationScope) }


    fun provideUserRepository(dataStore: DataStore<Preferences>):UserRepository{
        val userAuthManager = UserAuthManager.getInstance(dataStore)
        return UserRepository.getInstance(database.getUserDao(), userAuthManager)
    }

    fun provideItemRepository():ItemRepository{
        return ItemRepository.getInstance(database.getItemDao(), database.getFavoriteDao())
    }

    fun provideTransactionRepository():TransactionRepository{
        return TransactionRepository.getInstance(database.getTransactionDao(), database.getUserDao(), database.getItemDao())
    }


}