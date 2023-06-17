package com.example.tradeu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["id_user", "id_barang"])
data class Favorite(
    @ColumnInfo(name="id_user")
    val userId:Long,
    @ColumnInfo(name="id_barang", index = true)
    val itemId:Long
)