package com.example.tradeu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Transaction
    @Query("UPDATE User SET saldo = ((SELECT saldo from User WHERE id_user = :userId) - :totalPayment) WHERE id_user = :userId")
    suspend fun updateUserWallet(userId:Long, totalPayment:Long)
    @Query("SELECT EXISTS (SELECT * FROM User Where username = :username AND password = :password)")
    suspend fun isUsernameAndPasswordAlreadyUsed(username:String, password: String):Boolean

    @Insert
    suspend fun register(user: User):Long

    @Query("UPDATE User SET foto_profil = :newImgUriString WHERE id_user = :userId")
    suspend fun updateProfilePhoto(userId:Long, newImgUriString:String)



}