package app.deadmc.devnetworktool.helpers

import com.crashlytics.android.Crashlytics

inline fun safe(function : ()-> Unit) {
    try {
        function()
    } catch (e: Exception) {
        Crashlytics.logException(e)
    }
}
