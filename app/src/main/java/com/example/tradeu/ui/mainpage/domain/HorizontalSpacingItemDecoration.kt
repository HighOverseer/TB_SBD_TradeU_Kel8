package com.example.tradeu.ui.mainpage.domain

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class HorizontalSpacingItemDecoration(private val horizontalSpacing:Int) :ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = horizontalSpacing
    }
}