package com.example.tradeu.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tradeu.data.entities.Favorite
import com.example.tradeu.data.entities.Item
import com.example.tradeu.data.entities.User

data class ItemAndFavorite(
    @Embedded
    val favorite: Favorite,
    @Relation(
        entity = Item::class,
        parentColumn = "id_barangg",
        entityColumn = "id_barang")
    val itemAndUser: ItemAndUser,
)