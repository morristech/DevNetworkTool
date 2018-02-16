package app.deadmc.devnetworktool.helpers

import android.util.Log

inline fun safe(function : ()-> Unit) {
    try {
        function()
    } catch (e: Exception) {
        Log.e("safe",Log.getStackTraceString(e))
        //Crashlytics.logException(e)
    }
}
