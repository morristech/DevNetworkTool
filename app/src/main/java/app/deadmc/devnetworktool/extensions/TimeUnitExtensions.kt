package app.deadmc.devnetworktool.extensions

import java.util.concurrent.TimeUnit

fun TimeUnit.convertToString():String {
    var result = "SECONDS"
    when (this) {
        TimeUnit.DAYS -> result = "DAYS"
        TimeUnit.HOURS -> result = "HOURS"
        TimeUnit.MINUTES -> result = "MINUTES"
        TimeUnit.SECONDS -> result = "SECONDS"
        TimeUnit.MICROSECONDS -> result = "MICROSECONDS"
        TimeUnit.MILLISECONDS -> result = "MILLISECONDS"
        TimeUnit.NANOSECONDS -> result = "NANOSECONDS"
    }
    return result
}