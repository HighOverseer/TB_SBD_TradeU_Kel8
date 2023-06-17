package com.example.tradeu.data.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.tradeu.data.entities.Favorite
import com.example.tradeu.data.entities.Item
import com.example.tradeu.data.entities.User

data class ItemUserAndFavorite(
    @Embedded
    val item:Item,

    @Relation(
        parentColumn = "id_barang",
        entity = User::class,
        entityColumn = "id_user",
        associateBy = Junction(
            value = Favorite::class,
            parentColumn = "id_barang",
            entityColumn = "id_user"
        )
    )
    val listUser:List<User>,

    @Relation(parentColumn = "id_penjual", entityColumn = "id_user")
    val penjual:User

){
    val listUserCount:Int get() {return listUser.size}
}