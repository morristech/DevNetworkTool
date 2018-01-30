package app.deadmc.devnetworktool.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object DevPreferences {
    private var sharedPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    val SHARED_PREFERENCES_NAME = "DevNetworkToolPreferences"

    val PREFERENCES_FIRST_LAUNCH = "firstLaunch"
    val PREFERENCES_PING_DELAY = "pingDelay"

    /**
     * provides information about is it first launch or not
     * @return true if first launch, false if is not
     */
    val firstLaunch: Boolean
        get() = sharedPreferences!!.getBoolean(PREFERENCES_FIRST_LAUNCH, true)

    /**
     * provides information about currently set delay for ping requests
     * @return delay for ping requests in milliseconds
     */
    var pingDelay: Int
        get() = sharedPreferences!!.getInt(PREFERENCES_PING_DELAY, 1000)
        set(value) {
            sharedPreferences?.edit()?.putInt(PREFERENCES_PING_DELAY, value)?.apply()
        }

    /**
     * required to call init in Application class
     * @param context
     */
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun setFirstLaunch(value: Boolean) {
        sharedPreferences!!.edit().putBoolean(PREFERENCES_FIRST_LAUNCH, value).apply()
    }
}
