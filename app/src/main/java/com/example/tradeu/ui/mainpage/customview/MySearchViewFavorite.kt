package com.example.tradeu.ui.mainpage.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.tradeu.R

class MySearchViewFavorite:SearchView{
    private val mPaint = Paint()
    private lateinit var hintText:String
    private val colorWhite = ResourcesCompat.getColor(resources, R.color.white_100, null)
    private val colorGrey = ResourcesCompat.getColor(resources, R.color.grey_400, null)
    constructor(context: Context):super(context){
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet):super(context, attrs){
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleInt:Int):super(context, attrs, defStyleInt){
        init(context)
    }

    private fun init(context: Context){
        setIconifiedByDefault(false)
        background = ResourcesCompat.getDrawable(resources, R.drawable.search_view_favorite_bg, null)
        hintText = context.getString(R.string.hint_search_view_favorite)
        mPaint.apply {
            textSize = 16f.toDp()
            color = colorGrey
            typeface = ResourcesCompat.getFont(context, R.font.poppins_regular)

        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!hasFocus() && query.isEmpty()){
            isActivated = false
            mPaint.color = colorGrey
            canvas?.drawText(hintText, 58f.toDp(), 28.5f.toDp(), mPaint)
            canvas?.drawBitmap(
                vectorToBitmap(
                    R.drawable.ic_search_bar,
                    ResourcesCompat.getColor(resources, R.color.grey_500, null)
                )!!,
                13f.toDp(),
                10f.toDp(),
                mPaint
            )
        }else if(hasFocus()){
            isActivated = true
            if (query.isBlank()){
                mPaint.color = colorWhite
                canvas?.drawText(hintText, 26f.toDp(), 28.5f.toDp(), mPaint)
            }

        }

    }


    private fun vectorToBitmap(@DrawableRes id:Int, @ColorInt color:Int): Bitmap?{
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null){
            Log.e("BitmapHelper", "Resource not found")
            return null
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    private fun Float.toDp():Float{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        )
    }


}