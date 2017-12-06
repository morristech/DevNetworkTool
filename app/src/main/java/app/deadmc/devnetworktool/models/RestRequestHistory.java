package app.deadmc.devnetworktool.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by adanilov on 22.03.2017.
 */

public class RestRequestHistory extends SugarRecord {
    private long timeLastUsage;
    private String url;
    private String method;
    private String headers;
    private String requests;

    public RestRequestHistory() {

    }

    public RestRequestHistory(String url, String method, ArrayList<KeyValueModel> headers,ArrayList<KeyValueModel> requests) {
        Gson gson = new Gson();
        this.timeLastUsage = System.currentTimeMillis();
        this.url = url;
        this.method = method;
        this.headers = gson.toJson(headers);
        this.requests = gson.toJson(requests);
    }

    public long getTimeLastUsage() {
        return timeLastUsage;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public ArrayList<KeyValueModel> getHeaders() {
        return getArrayList(headers);
    }

    public ArrayList<KeyValueModel> getRequests() {
        return getArrayList(requests);
    }

    private ArrayList<KeyValueModel> getArrayList(String stringValue) {
        if (stringValue.isEmpty()) return new ArrayList<>();
        Type type = new TypeToken<ArrayList<KeyValueModel>>(){}.getType();
        Gson gson = new Gson();
        ArrayList<KeyValueModel> arrayList = gson.fromJson(stringValue, type);
        return arrayList;
    }




}
