package com.example.tradeu.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tradeu.data.entities.Item
import com.example.tradeu.data.entities.User

data class ItemAndUser(
    @Embedded
    val item:Item,

    @Relation(parentColumn = "id_penjual", entityColumn = "id_user")
    val user:User
)
