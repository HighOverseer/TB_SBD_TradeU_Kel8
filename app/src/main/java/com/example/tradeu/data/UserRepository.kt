package com.example.tradeu.data

import androidx.lifecycle.LiveData
import com.example.tradeu.data.dao.UserDao
import com.example.tradeu.data.database.TradeUDatabase
import com.example.tradeu.data.entities.*
import com.example.tradeu.data.preference.UserAuthManager
import java.lang.reflect.ParameterizedType

class UserRepository private constructor(
    private val userDao: UserDao,
    private val userAuthManager: UserAuthManager
){

    fun getListUser():LiveData<List<User>>{
        return userDao.getListUsers()
    }

    suspend fun login(username:String, password:String):Long?{
        return userDao.login(username, password)
    }

    fun getUserData(userId: Long):LiveData<User>{
        return userDao.getUserDataById(userId)
    }



    suspend fun getUserId():Long{
        return userAuthManager.getUserId()
    }

    suspend fun saveUserId(userId:Long){
        userAuthManager.saveUserId(userId)
    }

    suspend fun deleteUserId(){
        userAuthManager.deleteUserId()
    }

    companion object{
        @Volatile
        private var INSTANCE:UserRepository?=null

        fun getInstance(
            userDao: UserDao,
            userAuthManager: UserAuthManager
        ):UserRepository{
            return INSTANCE?: synchronized(this){
                INSTANCE?: UserRepository(userDao, userAuthManager)
            }.also { INSTANCE = it }
        }
    }
}