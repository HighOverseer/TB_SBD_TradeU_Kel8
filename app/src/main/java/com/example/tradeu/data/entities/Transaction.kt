package com.example.tradeu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tradeu.data.converter.DateStringConverter
import java.util.*

@Entity(tableName = "Transaksi")
data class Transaction(
    @PrimaryKey
    @ColumnInfo(name="no_transaksi")
    val transactionNumber:Long,
    @ColumnInfo(name="tgl_transaksi")
    val transactionDate: Date,
    @ColumnInfo(name="id_pembeli")
    val buyerId:Long,
    @ColumnInfo(name="id_barang")
    val itemId:Long,
    @ColumnInfo(name="jumlah")
    val quantity:Short,
    @ColumnInfo(name="harga_saat_dibeli")
    val price:Long,
    @ColumnInfo(name="id_ukuran")
    val sizeId:Long
)
