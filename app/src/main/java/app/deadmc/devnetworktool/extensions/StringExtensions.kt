package app.deadmc.devnetworktool.extensions

import java.util.concurrent.TimeUnit

inline fun String.toTimeUnit(): TimeUnit {
    var result = TimeUnit.SECONDS
    when (this) {
        "DAYS" -> result = TimeUnit.DAYS
        "HOURS" -> result = TimeUnit.HOURS
        "MINUTES" -> result = TimeUnit.MINUTES
        "SECONDS" -> result = TimeUnit.SECONDS
        "MICROSECONDS" -> result = TimeUnit.MICROSECONDS
        "MILLISECONDS" -> result = TimeUnit.MILLISECONDS
        "NANOSECONDS" -> result = TimeUnit.NANOSECONDS
    }
    return result
}