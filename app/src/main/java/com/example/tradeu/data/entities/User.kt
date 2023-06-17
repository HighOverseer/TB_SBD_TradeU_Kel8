package com.example.tradeu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id_user")
    val userId:Long,
    @ColumnInfo(name = "username")
    var username:String,
    @ColumnInfo(name="password")
    var password:String,
    @ColumnInfo(name="foto_profil")
    var profilePhoto:Int,
    @ColumnInfo(name="nama")
    var name:String,
    @ColumnInfo(name="saldo")
    var balance:Long,
    @ColumnInfo(name="alamat")
    var address:String
)