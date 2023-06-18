package com.example.tradeu

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.os.ConfigurationCompat
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


const val AUTHORITY = "com.example.tradeu"
private const val PHOTO = "photo_"
fun showToast(context: Context, message:String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun formatBalanceString(balance:Long):String{
    val unformattedBalance = balance.toString()
    return unformattedBalance.reversed().chunked(3).joinToString(".").reversed()
}

suspend fun saveUriToFile(selectedImg: Uri, context: Context): String? = withContext(Dispatchers.IO){
    val contentResolver: ContentResolver = context.contentResolver
    val fileNameFormat = SimpleDateFormat("yyyyMMddHmmss", ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0])
    val myFile = createCustomTempFile(context, "IMG-${fileNameFormat.format(Calendar.getInstance().time)}")

    val inputStream = contentResolver.openInputStream(selectedImg)
    if (inputStream != null) {
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()
        val uriString = Uri.fromFile(myFile).toString()
        uriString
    }else null
}

fun String.formatToDate():Date?{
    val format = SimpleDateFormat("yyyy/MM/dd", ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0])
    return format.parse(this)
}

fun Date.toFormatString():String{
    val format = SimpleDateFormat("dd MMMM yyyy", ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0])
    return format.format(this)
}

fun getCurrentDateInFormat():String{
    val format = SimpleDateFormat("yyyy/MM/dd", ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0])
    val c = Calendar.getInstance()
    val currentDate = c.time
    return format.format(currentDate)
}

fun getCurrentDate(): Date {
    val c = Calendar.getInstance()
    return c.time
}

fun CircleImageView.loadImage(context:Context, imageStringUri:String){
    Glide.with(context)
        .load(Uri.parse(imageStringUri))
        .into(this)
}

fun AppCompatImageView.loadImage(context:Context, imageStringUri:String){
    Glide.with(context)
        .load(Uri.parse(imageStringUri))
        .into(this)
}

fun AppCompatImageView.loadImage(context:Context, imageId:Int){
    Glide.with(context)
        .load(imageId)
        .into(this)
}


fun ImageView.loadImage(context:Context, imageStringUri:String){
    Glide.with(context)
        .load(Uri.parse(imageStringUri))
        .into(this)
}



fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPictByteArray = bmpStream.toByteArray()
        streamLength = bmpPictByteArray.size
        compressQuality -= 5
    } while (streamLength > 2000_000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}
suspend fun savingDrawableToStorage(context: Context, drawableId:Int):String? =
    withContext(Dispatchers.IO){
        var imgStringUri:String?=null
        val imgBitmap = BitmapFactory.decodeResource(context.resources, drawableId)
        val fileNameFormat = SimpleDateFormat("yyyyMMddHmmss", ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0])
        val file = createCustomTempFile(context, "IMG-${fileNameFormat.format(Calendar.getInstance().time)}")
        var fos:FileOutputStream?=null
        try {
            fos = FileOutputStream(file)
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            val compressedFile = reduceFileImage(file)
            imgStringUri = Uri.fromFile(compressedFile).toString()
        } catch (e: IOException) {
            println(e.message.toString())
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    println(e.message.toString())
                }
            }
        }
        imgStringUri
    }




fun createCustomTempFile(context: Context, filename:String?= PHOTO): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(filename, ".jpg", storageDir)
}