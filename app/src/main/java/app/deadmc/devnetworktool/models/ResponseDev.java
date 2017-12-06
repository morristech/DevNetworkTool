package app.deadmc.devnetworktool.models;

/**
 * Created by adanilov on 20.03.2017.
 */

public class ResponseDev {

    private String headers;
    private String body;
    private int code;
    private int delay;

    public ResponseDev() {

    }

    public ResponseDev(String headers,String body, int code, int delay) {
        this.headers = headers;
        this.body = body;
        this.code = code;
        this.delay = delay;
    }


    public int getDelay() {
        return delay;
    }

    public String getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public int getCode() {
        return code;
    }


}
