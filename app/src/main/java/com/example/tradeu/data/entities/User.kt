package com.example.tradeu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_user")
    val userId:Long=0,
    @ColumnInfo(name = "username")
    var username:String,
    @ColumnInfo(name="password")
    var password:String,
    @ColumnInfo(name="foto_profil")
    var profilePhoto:String,
    @ColumnInfo(name="nama")
    var name:String,
    @ColumnInfo(name="saldo")
    var balance:Long,
    @ColumnInfo(name="alamat")
    var address:String
)