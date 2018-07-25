package app.deadmc.devnetworktool.data.shared_preferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesImpl(val context: Context) : Preferences {
    private var sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    /**
     * provides information about is it first launch or not
     * @return true if first launch, false if is not
     */
    override var firstLaunch: Boolean
        get() = sharedPreferences!!.getBoolean(PREFERENCES_FIRST_LAUNCH, true)
        set(value) {
            sharedPreferences!!.edit().putBoolean(PREFERENCES_FIRST_LAUNCH, value).apply()
        }

    /**
     * provides information about currently set delay for ping requests
     * @return delay for ping requests in milliseconds
     */
    override var pingDelay: Int
        get() = sharedPreferences!!.getInt(PREFERENCES_PING_DELAY, 1000)
        set(value) {
            sharedPreferences?.edit()?.putInt(PREFERENCES_PING_DELAY, value)?.apply()
        }

    /**
     * clears Edit Text after sending information. Works for TCP/UDP
     */
    override var clearEditTextAfterSend
        get() = sharedPreferences!!.getBoolean(PREFERENCES_CLEAR_EDIT_TEXT_AFTER_SEND, false)
        set(value) {
            sharedPreferences?.edit()?.putBoolean(PREFERENCES_CLEAR_EDIT_TEXT_AFTER_SEND, value)?.apply()
        }

    override var disableSsl
        get() = sharedPreferences!!.getBoolean(PREFERENCES_DISABLE_SSL, false)
        set(value) {
            sharedPreferences?.edit()?.putBoolean(PREFERENCES_DISABLE_SSL, value)?.apply()
        }

    override var restTimeoutAmount: Long
        get() = sharedPreferences!!.getLong(PREFERENCES_REST_TIMEOUT_AMOUNT, 1000L)
        set(value) {
            sharedPreferences?.edit()?.putLong(PREFERENCES_REST_TIMEOUT_AMOUNT, value)?.apply()
        }


    override var tcpUdpEncoding
        get() = sharedPreferences!!.getString(PREFERENCES_TCP_UDP_ENCODING, "UTF-8")
        set(value) {
            sharedPreferences?.edit()?.putString(PREFERENCES_TCP_UDP_ENCODING, value)?.apply()
        }

    override var tcpTimeoutAmount: Int
        get() = sharedPreferences!!.getInt(PREFERENCES_TCP_TIMEOUT_AMOUNT, 2000)
        set(value) {
            sharedPreferences?.edit()?.putInt(PREFERENCES_TCP_TIMEOUT_AMOUNT, value)?.apply()
        }


}
