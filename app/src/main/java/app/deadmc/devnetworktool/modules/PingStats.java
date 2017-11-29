package app.deadmc.devnetworktool.modules;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Feren on 30.11.2016.
 */
public class PingStats {

    private int sended;
    private int successful;
    private int failed;
    private int ttl;

    private float max;
    private float min;
    private float average;

    private float all;

    private String url;
    private String ipAddress;

    public PingStats() {
        sended = 0;
        successful = 0;
        failed = 0;
        ttl = 0;
        max = 0f;
        min = 0f;
        average = 0f;
        all = 0f;
        ipAddress = "";
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public void addPing(float time) {
        sended++;
        if (time > 0.1f) {
            successful++;

            if (min == 0f)
                min = time;

            if (time < min)
                min = time;
            if (time > max)
                max = time;
            all+=time;
            average = all/sended;
        } else {
            failed++;
        }
    }

    public int getSended() {
        return sended;
    }

    public int getSuccessful() {
        return successful;
    }

    public int getFailed() {
        return failed;
    }

    public int getTtl() {
        return ttl;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    public float getAverage() {
        return Math.round(average*10)/10f;
    }

    public String getIpAddress() {
        return ipAddress;
    }

}
