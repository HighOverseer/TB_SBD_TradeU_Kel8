package com.example.tradeu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tradeu.data.entities.Favorite
import com.example.tradeu.data.entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListUser(listUser:List<User>)

    @Query("SELECT * FROM User")
    fun getListUsers():LiveData<List<User>>

    @Query("SELECT * FROM User WHERE id_user = :userId")
    fun getUserDataById(userId:Long):LiveData<User>

    @Query("SELECT id_user FROM User WHERE username = :username AND password = :password")
    suspend fun login(username:String, password:String):Long?
}