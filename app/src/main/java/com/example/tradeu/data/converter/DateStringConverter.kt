package com.example.tradeu.data.converter

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import androidx.room.TypeConverter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class DateStringConverter {
    private val format = SimpleDateFormat("yyyy/MM/dd", ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0])

    @TypeConverter
    fun dateToString(date:Date):String{
        return format.format(date)
    }
    @TypeConverter
    fun stringToDate(value:String):Date?{
        return try {
            format.parse(value)
        }catch (e:ParseException){
            null
        }
    }
}