package app.deadmc.devnetworktool.modules;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Feren on 21.06.2016.
 */
public class ConnectionHistory extends SugarRecord implements Serializable{

    private int port;
    private int lastUsage;
    private String ipAddress;
    private String name;
    private String lastUsageTime;
    private String type;


    public ConnectionHistory() {

    }

    public ConnectionHistory(String name, String ipAddress, int port, String type) {
        this.name = name;
        this.port = port;
        this.ipAddress = ipAddress;
        this.type = type;
        setLastUsageDefault();
    }

    public String getType() {return type; }
    public void setType(String type) {this.type = type;}

    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() { return port; }
    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getLastUsage() {
        return lastUsage;
    }
    public void setLastUsage(int lastUsage) {
        this.lastUsage = lastUsage;
    }
    public void setLastUsageDefault() {
        Long epoch = System.currentTimeMillis()/1000;
        this.lastUsage = epoch.intValue();
    }

    public String getLastUsageTime() {
        return lastUsageTime;
    }
    public void setLastUsageTime(String lastUsageTime) {
        this.lastUsageTime = lastUsageTime;
    }

    public boolean isEmpty() {
        return ipAddress.isEmpty();
    }
}
