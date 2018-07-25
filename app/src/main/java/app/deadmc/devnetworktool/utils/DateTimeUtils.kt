package app.deadmc.devnetworktool.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

val TIME_FORMAT: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
val DATE_TIME_FORMAT: DateFormat = SimpleDateFormat("MMM d, yyyy HH:mm:ss",Locale.getDefault())
val DATE_FORMAT: DateFormat = SimpleDateFormat("dd.MM.yyyy",Locale.getDefault())
val currentTime: String
    get() {
        val date = Date()
        return TIME_FORMAT.format(date)
    }

fun getTimeFromTimestamp(timestamp: Long): String {
    safe {
        val date = Date(timestamp)
        return TIME_FORMAT.format(date)
    }
    return ""
}

fun getDateTimeFromTimestamp(timestamp: Long): String {
    try {
        val date = Date(timestamp)
        return DATE_TIME_FORMAT.format(date)
    } catch (e:Exception) {}
    return ""
}




