package app.deadmc.devnetworktool.helpers

import android.content.Context
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

val TIME_FORMAT: DateFormat = SimpleDateFormat("HH:mm:ss")
val DATE_TIME_FORMAT: DateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
val DATE_FORMAT: DateFormat = SimpleDateFormat("dd.MM.yyyy")
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

fun getDateFromTimestamp(timestamp: Long, context: Context): String {
    //Log.e("timestamp",""+timestamp);
    val date = Date(timestamp)
    val dateFormat = android.text.format.DateFormat.getDateFormat(context)
    return dateFormat.format(date)
}

fun getDateAndTime(timestamp: Long, context: Context): String {
    safe {
        val date = Date(timestamp)
        val simpleDateFormat = SimpleDateFormat()
        val dateLocalizedFormatPattern = simpleDateFormat.toLocalizedPattern()
        val currentDateFormat: DateFormat = SimpleDateFormat(dateLocalizedFormatPattern)
        return currentDateFormat.format(date)
    }
    return ""
    /*
    val date = getDateFromTimestamp(timestamp, context)
    val time = getTimeFromTimestamp(timestamp)
    return date + " " + time
    */

}



