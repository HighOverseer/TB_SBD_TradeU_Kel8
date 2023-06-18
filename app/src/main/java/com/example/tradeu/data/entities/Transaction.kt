package com.example.tradeu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tradeu.data.converter.DateStringConverter
import com.example.tradeu.getCurrentDate
import java.util.*

@Entity(tableName = "Transaksi")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="no_transaksi")
    val transactionNumber:Long = 0,
    @ColumnInfo(name="tgl_transaksi")
    val transactionDate: Date = getCurrentDate(),
    @ColumnInfo(name="id_pembeli")
    val buyerId:Long,
    @ColumnInfo(name = "nama_pembeli")
    val buyerName:String,
    @ColumnInfo(name = "alamat_pembeli")
    val buyerAddress:String,
    @ColumnInfo(name = "kontak_pembeli")
    val buyerContact:String,
    @ColumnInfo(name="id_barang")
    val itemId:Long,
    @ColumnInfo(name="jumlah")
    val quantity:Short,
    @ColumnInfo(name="harga_saat_dibeli")
    val itemPrice:Long,
    @ColumnInfo(name = "total")
    val total:Long,
    @ColumnInfo(name="id_ukuran")
    val sizeId:Long
)
