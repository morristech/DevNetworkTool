package app.deadmc.devnetworktool.models

import android.content.Context

import app.deadmc.devnetworktool.R

import app.deadmc.devnetworktool.helpers.getIpAddressFromString
import app.deadmc.devnetworktool.helpers.getPingFromString
import app.deadmc.devnetworktool.helpers.getTtlFromString

class PingStructure(val rawString: String) {

    val ping: Float
    val ttl: Int

    val ipAddress: String
    var timeAdded: Int = 0
        private set


    init {
        this.ping = getPingFromString(rawString)
        this.ipAddress = getIpAddressFromString(rawString)
        this.ttl = getTtlFromString(rawString)
        setTimeAddedDefault()
    }

    fun setTimeAddedDefault() {
        val epoch = System.currentTimeMillis() / 1000
        this.timeAdded = epoch.toInt()
    }

    fun stringForRecyclerView(context: Context): String {
        //Log.e("raw string",rawString);
        val pingInt = Math.round(ping)
        /*
        String recyclerViewMessage =
                context.getString(R.string.from)+" "+ipAddress+
                " ttl = "+ttl+
                " "+context.getString(R.string.time)+" = "+ping;
                */

        var recyclerViewMessage = context.getString(R.string.time_ms, pingInt)
        if (ping == 0f)
            recyclerViewMessage = context.getString(R.string.packet_lost)
        return recyclerViewMessage
    }


}
