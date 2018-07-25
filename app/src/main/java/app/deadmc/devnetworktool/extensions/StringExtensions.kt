package app.deadmc.devnetworktool.extensions

import app.deadmc.devnetworktool.utils.safe
import java.util.concurrent.TimeUnit

fun String.toTimeUnit(): TimeUnit {
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

fun String?.isValidIp(): Boolean {
    try {
        if (this == null || this.isEmpty()) {
            return false
        }

        val parts = this.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (parts.size != 4) {
            return false
        }

        for (s in parts) {
            val i = Integer.parseInt(s)
            if (i < 0 || i > 255) {
                return false
            }
        }
        return !this.endsWith(".")
    } catch (nfe: NumberFormatException) {
        return false
    }

}

fun String.portFromString(): Int {
    var port = 80
    if (!this.isEmpty()) {
        safe {
            port = Integer.parseInt(this)
        }
    }
    return port
}

fun String.toCapitalizedFirstLetterString():String {
    return this.substring(0, 1).toUpperCase() + this.substring(1).toLowerCase()
}
