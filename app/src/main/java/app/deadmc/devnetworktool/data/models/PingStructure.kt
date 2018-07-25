package app.deadmc.devnetworktool.data.models

import android.content.Context

import app.deadmc.devnetworktool.R

import app.deadmc.devnetworktool.utils.getIpAddressFromString
import app.deadmc.devnetworktool.utils.getPingFromString
import app.deadmc.devnetworktool.utils.getTtlFromString

class PingStructure(val rawString: String) {

    val ping: Float
    val ttl: Int

    val ipAddress: String
    var timeAdded: Long = 0
        private set


    init {
        this.ping = getPingFromString(rawString)
        this.ipAddress = getIpAddressFromString(rawString)
        this.ttl = getTtlFromString(rawString)
        this.timeAdded = System.currentTimeMillis()
    }

    fun stringForRecyclerView(context: Context): String {
        val pingInt = Math.round(ping)
        var recyclerViewMessage = context.getString(R.string.time_ms, pingInt)
        if (ping == 0f)
            recyclerViewMessage = context.getString(R.string.packet_lost)
        return recyclerViewMessage
    }


}
