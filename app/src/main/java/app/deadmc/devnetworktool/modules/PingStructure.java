package app.deadmc.devnetworktool.modules;

import android.content.Context;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.helpers.StringHelper;

/**
 * Created by Feren on 14.11.2016.
 */
public class PingStructure {

    private float ping;
    private int ttl;



    private String ipAddress;
    private int timeAdded;
    private String rawString;


    public PingStructure(String rawString) {
        this.ping = StringHelper.getPingFromString(rawString);
        this.ipAddress = StringHelper.getIpAddressFromString(rawString);
        this.ttl = StringHelper.getTtlFromString(rawString);
        this.rawString = rawString;
        setTimeAddedDefault();
    }

    public float getPing() {
        return ping;
    }

    public int getTimeAdded() {
        return timeAdded;
    }

    public String getRawString() {
        return rawString;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTimeAddedDefault() {
        Long epoch = System.currentTimeMillis()/1000;
        this.timeAdded = epoch.intValue();
    }

    public String stringForRecyclerView(Context context) {
        //Log.e("raw string",rawString);
        int pingInt = Math.round(ping);
        /*
        String recyclerViewMessage =
                context.getString(R.string.from)+" "+ipAddress+
                " ttl = "+ttl+
                " "+context.getString(R.string.time)+" = "+ping;
                */

        String recyclerViewMessage = context.getString(R.string.time_ms,pingInt);
        if (ping == 0f)
            recyclerViewMessage = context.getString(R.string.packet_lost);
        return recyclerViewMessage;
    }




}
