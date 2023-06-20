package com.example.mynotes.presentation.utils.extensions

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

fun Double.huminize(): String {
    val diff = this - this.toLong()
    val dec = if (diff >= 0.01 && this <= 9_999_999)
        DecimalFormat("###,###,###,###,###.00", DecimalFormatSymbols(Locale.ENGLISH))
    else
        DecimalFormat("###,###,###,###,###", DecimalFormatSymbols(Locale.ENGLISH))

    var formattedNumber = dec.format(this).replace(",", " ")
    if (formattedNumber.first() == '.') {
        formattedNumber = "0$formattedNumber"
    }
    return formattedNumber
    // return if (diff > 0) String.format("%.2f", this) else this.toLong().toString()
}

fun Double.roundTen(): String {
    val dec =
        DecimalFormat("###,###,###,###,###.0", DecimalFormatSymbols(Locale.ENGLISH))
    return dec.format(this).replace(",", " ")
}

fun Long.huminize(): String {
    val diff = System.currentTimeMillis() - this
    val format = "dd.MM.yyyy  HH:mm"
    val formatted = if (diff < 180_000) {
        "hozirgina"
    } else if (diff < 3600_000) {
        "${diff / 60_000} min oldin"
    } else if (diff < 86_400_000) {
        "${diff / 3600_000} soat oldin"
    } else {
        SimpleDateFormat(format, Locale.getDefault()).format(Date(this))
    }
    return formatted
}

fun Long.huminizeForFile() =
    SimpleDateFormat("dd.MM.yyyy  HH:mm:ss", Locale.getDefault()).format(Date(this))

fun Double.round() = Math.round(this * 100.0) / 100.0
//fun String.huminize(): String {
//    var text = this
//    try {
//        text = text.toDouble().huminize()
//    } catch (_: Exception) {
//    }
//    return text
//}

fun ByteArray.huminize(): String {
    val kbyte = 1024
    val mbyte = 1024 * 1024
    val gbyte = 1024 * 1024 * 1024
    val value = if (this.size < kbyte) "${this} byte"
    else if (this.size < 700 * kbyte) "${(this.size.toDouble() / kbyte).roundTen()} Kb"
    else if (this.size < 700 * mbyte) "${(this.size.toDouble() / mbyte).roundTen()} Mb"
    else "${(this.size.toDouble() / gbyte).roundTen()} Gb"
    return value
}




