package app.deadmc.devnetworktool.data.shared_preferences

import android.content.SharedPreferences

const val SHARED_PREFERENCES_NAME = "DevNetworkToolPreferences"
const val PREFERENCES_FIRST_LAUNCH = "firstLaunch"
const val PREFERENCES_PING_DELAY = "pingDelay"
const val PREFERENCES_REST_TIMEOUT_AMOUNT = "restTimeoutAmount"
const val PREFERENCES_TCP_TIMEOUT_AMOUNT = "tcpTimeoutAmount"
const val PREFERENCES_CLEAR_EDIT_TEXT_AFTER_SEND = "clearEditTextAfterSend"
const val PREFERENCES_TCP_UDP_ENCODING = "tcpUdpEncoding"
const val PREFERENCES_DISABLE_SSL = "disableSsl"

interface Preferences {
    var firstLaunch: Boolean
    var pingDelay: Int
    var clearEditTextAfterSend: Boolean
    var disableSsl: Boolean
    var restTimeoutAmount: Long
    var tcpUdpEncoding: String
    var tcpTimeoutAmount: Int
}