package app.deadmc.devnetworktool.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import app.deadmc.devnetworktool.helpers.getStringFromTimeUnit
import app.deadmc.devnetworktool.helpers.getTimeUnitFromString
import java.util.concurrent.TimeUnit

object DevPreferences {
    private var sharedPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    val SHARED_PREFERENCES_NAME = "DevNetworkToolPreferences"

    val PREFERENCES_FIRST_LAUNCH = "firstLaunch"
    val PREFERENCES_PING_DELAY = "pingDelay"
    val PREFERENCES_REST_TIMEOUT_AMOUNT = "restTimeoutAmount"
    val PREFERENCES_REST_TIMEOUT_UNIT = "restTimeoutUnit"
    val PREFERENCES_CLEAR_EDIT_TEXT_AFTER_SEND = "clearEditTextAfterSend"
    val PREFERENCES_DISABLE_SSL = "disableSsl"

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
     * clears Edit Text after sending information. Works for TCP/UDP
     */
    var clearEditTextAfterSend
        get() = sharedPreferences!!.getBoolean(PREFERENCES_CLEAR_EDIT_TEXT_AFTER_SEND, false)
        set(value) {
            sharedPreferences?.edit()?.putBoolean(PREFERENCES_CLEAR_EDIT_TEXT_AFTER_SEND, value)?.apply()
        }

    var disableSsl
        get() = sharedPreferences!!.getBoolean(PREFERENCES_DISABLE_SSL, false)
        set(value) {
            sharedPreferences?.edit()?.putBoolean(PREFERENCES_DISABLE_SSL, value)?.apply()
        }

    var restTimeoutAmount: Long
        get() = sharedPreferences!!.getLong(PREFERENCES_REST_TIMEOUT_AMOUNT, 1000L)
        set(value) {
            sharedPreferences?.edit()?.putLong(PREFERENCES_REST_TIMEOUT_AMOUNT, value)?.apply()
        }

    var restTimeoutUnit: TimeUnit
        get() {
            val value = sharedPreferences!!.getString(PREFERENCES_REST_TIMEOUT_UNIT, "SECONDS")
            return getTimeUnitFromString(value)
        }
        set(value) {
            sharedPreferences?.edit()?.putString(PREFERENCES_REST_TIMEOUT_UNIT, getStringFromTimeUnit(value))?.apply()
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
