package com.example.tradeu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ukuran_Pakaian")
data class ItemSize(
    @PrimaryKey
    @ColumnInfo(name="id_ukuran")
    val sizeId:Long,
    @ColumnInfo(name="kode_ukuran")
    var sizeCode:String
)