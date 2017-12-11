package app.deadmc.devnetworktool.models;

import com.orm.SugarRecord;

public class MessageHistory extends SugarRecord{
    private long timeAdded;
    private int port;
    private boolean fromServer;

    private String ipAddress;
    private String message;
    private String type;

    public MessageHistory() {

    }

    public MessageHistory(String message, String ipAddress, int port, String type, boolean fromServer) {
        this.message = message;
        this.ipAddress = ipAddress;
        this.port = port;
        this.type = type;
        this.fromServer = fromServer;
        setTimeAddedDefault();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(long timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimeAddedDefault() {
        this.timeAdded = System.currentTimeMillis();
    }

    public boolean isFromServer() {
        return fromServer;
    }

    public void setFromServer(boolean fromServer) {
        this.fromServer = fromServer;
    }





}
