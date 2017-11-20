package app.deadmc.devnetworktool.singletons;

import android.content.Context;

import app.deadmc.devnetworktool.modules.ResponseDev;

/**
 * Created by Feren on 23.03.2017.
 */

public class SharedData {

    private ResponseDev responseDev;

    private Context context;

    private static SharedData instance;

    public static void init(Context context) {
        instance = new SharedData();
        instance.setContext(context);
    }

    public static SharedData getInstance() {
        return instance;
    }

    public ResponseDev getResponseDev() {
        return responseDev;
    }

    public void setResponseDev(ResponseDev responseDev) {
        this.responseDev = responseDev;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

}
