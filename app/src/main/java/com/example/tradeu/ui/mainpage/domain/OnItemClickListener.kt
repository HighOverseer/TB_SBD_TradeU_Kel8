package com.example.tradeu.ui.mainpage.domain

import androidx.core.app.ActivityOptionsCompat
import com.example.tradeu.data.entities.Favorite

interface OnItemClickListener<T>{
    fun onClick(value: T, sharedElementTransition:ActivityOptionsCompat)
    fun updateFavoriteStatus(itemId:Long, isFavorite:Boolean)
}