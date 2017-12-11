package app.deadmc.devnetworktool.helpers

import android.content.Context

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

val TIME_FORMAT: DateFormat = SimpleDateFormat("HH:mm:ss")
val DATE_TIME_FORMAT: DateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
val DATE_FORMAT: DateFormat = SimpleDateFormat("dd.MM.yyyy")
val currentTime: String
    get() {
        val date = Date()
        return TIME_FORMAT.format(date)
    }

fun getTimeFromTimestamp(timestamp: Long): String {
    //Log.e("timestamp",""+timestamp);
    val date = Date(timestamp)
    return TIME_FORMAT.format(date)
}

fun getDateTimeFromTimestamp(timestamp: Long): String {
    //Log.e("timestamp",""+timestamp);
    val date = Date(timestamp)
    return DATE_TIME_FORMAT.format(date)
}

fun getDateFromTimestamp(timestamp: Long, context: Context): String {
    //Log.e("timestamp",""+timestamp);
    val date = Date(timestamp)
    val dateFormat = android.text.format.DateFormat.getDateFormat(context)
    return dateFormat.format(date)
}

fun getDateAndTime(timestamp: Long, context: Context): String {
    val date = getDateFromTimestamp(timestamp, context)
    val time = getTimeFromTimestamp(timestamp)
    //return time+System.getProperty("line.separator")+date;
    return date + " " + time
}
