package com.salesmotoris

import com.squareup.moshi.Moshi
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Alexey on 07.10.2016.
 */
//EEE MMM dd HH:mm:ss zzz yyyy

fun String.formatDate(pattern: String): String {
    val initialFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
    val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    val date = initialFormat.parse(this)
    return format.format(date).toString()
}

fun String.customFormat(patternInit: String, pattern: String): String {
    val initialFormat = SimpleDateFormat(patternInit, Locale.ENGLISH)
    val formatOutput = SimpleDateFormat(pattern, Locale("in", "ID"))
    val date = initialFormat.parse(this)
    return formatOutput.format(date).toString()
}

//fun Float.formatCurrency(): String{
//    val formatter = DecimalFormat("#,###.##")
//    return formatter.format(this)
//}

fun Float.formatCurrency(): String{
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return formatter.format(this).replace("Rp", "")
}

inline fun <reified T> Moshi.fromJson(json: String): T = this.adapter(T::class.java).fromJson(json)!!

