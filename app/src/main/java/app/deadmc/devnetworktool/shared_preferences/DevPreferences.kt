package app.deadmc.devnetworktool.shared_preferences

import android.content.Context
import android.content.SharedPreferences

object DevPreferences {
    private var sharedPreferences: SharedPreferences? = null
    private val SHARED_PREFERENCES_NAME = "DevNetworkToolPreferences"

    private val PREFERENCES_FIRST_LAUNCH = "firstLaunch"
    private val PREFERENCES_PING_DELAY = "pingDelay"
    private val PREFERENCES_REST_TIMEOUT_AMOUNT = "restTimeoutAmount"
    private val PREFERENCES_TCP_TIMEOUT_AMOUNT = "tcpTimeoutAmount"
    private val PREFERENCES_CLEAR_EDIT_TEXT_AFTER_SEND = "clearEditTextAfterSend"
    private val PREFERENCES_TCP_UDP_ENCODING = "tcpUdpEncoding"
    private val PREFERENCES_DISABLE_SSL = "disableSsl"

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


    var tcpUdpEncoding
        get() = sharedPreferences!!.getString(PREFERENCES_TCP_UDP_ENCODING, "UTF-8")
        set(value) {
            sharedPreferences?.edit()?.putString(PREFERENCES_TCP_UDP_ENCODING, value)?.apply()
        }

    var tcpTimeoutAmount: Int
        get() = sharedPreferences!!.getInt(PREFERENCES_TCP_TIMEOUT_AMOUNT, 2000)
        set(value) {
            sharedPreferences?.edit()?.putInt(PREFERENCES_TCP_TIMEOUT_AMOUNT, value)?.apply()
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
