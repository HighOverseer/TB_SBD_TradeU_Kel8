package com.example.tradeu.data.entities.relations

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.tradeu.data.entities.Favorite
import com.example.tradeu.data.entities.Item
import com.example.tradeu.data.entities.User
import java.util.*

data class ItemUserAndFavorite(
    @ColumnInfo(name="id_barang")
    val itemId:Long,
    @ColumnInfo(name="id_penjual")
    val sellerId:Long,
    @ColumnInfo(name="nama_barang")
    var itemName:String,
    @ColumnInfo(name="foto_produk")
    var itemPhoto:String,
    @ColumnInfo(name="harga")
    var price:Long,
    @ColumnInfo(name="stok")
    var stock:Short,
    @ColumnInfo(name="tgl_publish")
    var publishDate: Date,
    @ColumnInfo(name="deskripsi")
    var description:String,
    @ColumnInfo(name="status_favorite")
    val statusFavorite:Boolean,
    @ColumnInfo(name="alamat")
    val address:String
)