package com.example.tradeu.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tradeu.data.entities.Item
import com.example.tradeu.data.entities.ItemSize
import com.example.tradeu.data.entities.Transaction
import com.example.tradeu.data.entities.User

data class TransactionItemAndItemSize(
    @Embedded
    val transaction: Transaction,

    @Relation(parentColumn = "id_barang", entityColumn = "id_barang")
    val item:Item,

    @Relation(parentColumn = "id_ukuran", entityColumn = "id_ukuran")
    val itemSize:ItemSize

)