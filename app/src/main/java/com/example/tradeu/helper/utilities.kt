package com.example.tradeu

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.os.ConfigurationCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

fun showSnackbar(view: View, message:String){
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

fun formatBalanceString(balance:Long):String{
    val unformattedBalance = balance.toString()
    return unformattedBalance.reversed().chunked(3).joinToString(".").reversed()
}

fun String.formatToDate():Date?{
    val format = SimpleDateFormat("yyyy/MM/dd", ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0])
    return format.parse(this)
}

fun getCurrentDateInFormat():String{
    val format = SimpleDateFormat("yyyy/MM/dd", ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0])
    val c = Calendar.getInstance()
    val currentDate = c.time
    return format.format(currentDate)
}

fun CircleImageView.loadImage(context:Context, image:Int){
    Glide.with(context)
        .load(image)
        .into(this)
}

fun AppCompatImageView.loadImage(context:Context, image:Int){
    Glide.with(context)
        .load(image)
        .into(this)
}

fun ImageView.loadImage(context:Context, image:Int){
    Glide.with(context)
        .load(image)
        .into(this)
}