package app.deadmc.devnetworktool.utils

inline fun safe(function : ()-> Unit) {
    try {
        function()
    } catch (e: Exception) {
        //Log.e("safe",Log.getStackTraceString(e))
        //Crashlytics.logException(e)
    }
}
