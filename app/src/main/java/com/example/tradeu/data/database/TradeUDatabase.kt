package com.example.tradeu.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tradeu.data.converter.DateStringConverter
import com.example.tradeu.helper.InitialDataSource
import com.example.tradeu.data.dao.*
import com.example.tradeu.data.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(
    entities = [User::class, Item::class, Transaction::class, Favorite::class, ItemSize::class],
    version = 1
)
@TypeConverters(DateStringConverter::class)
abstract class TradeUDatabase:RoomDatabase() {
    abstract fun getUserDao():UserDao
    abstract fun getItemDao():ItemDao
    abstract fun getTransactionDao():TransactionDao
    abstract fun getFavoriteDao():FavoriteDao
    abstract fun getItemSizeDao():ItemSizeDao

    companion object{
        @Volatile
        private var INSTANCE:TradeUDatabase?=null
        @JvmStatic
        fun getDatabase(context:Context, applicationScope:CoroutineScope):TradeUDatabase{
            if (INSTANCE == null){
                synchronized(TradeUDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TradeUDatabase::class.java,
                        "tradeu_database"
                    ).addCallback(object: Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let{database ->
                                applicationScope.launch {
                                    withContext(Dispatchers.IO){
                                        database.getUserDao().insertListUser(InitialDataSource.getListUser(context))
                                        database.getItemDao().insertListItems(InitialDataSource.getListItems(context))
                                        database.getTransactionDao().insertListTransaction(InitialDataSource.getListTransaction())
                                        database.getFavoriteDao().insertListFavorites(InitialDataSource.getListFavorites())
                                        database.getItemSizeDao().insertListItemSizes(InitialDataSource.getListItemSizes())
                                    }
                                }
                            }
                        }
                    }).build()

                }
            }
            return INSTANCE as TradeUDatabase
        }
    }
}