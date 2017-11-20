package app.deadmc.devnetworktool.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.orm.SugarContext;

import app.deadmc.devnetworktool.constants.DevConsts;
import app.deadmc.devnetworktool.modules.ConnectionHistory;
import app.deadmc.devnetworktool.shared_preferences.DevPreferences;
import app.deadmc.devnetworktool.singletons.SharedData;
import io.fabric.sdk.android.Fabric;

/**
 * Created by DEADMC on 07.03.2017.
 */
@SuppressWarnings("unused")
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        SugarContext.init(getApplicationContext());
        DevPreferences.init(getApplicationContext());
        SharedData.init(getApplicationContext());

        if (DevPreferences.getFirstLaunch()) {
            createDefaultData();
            DevPreferences.setFirstLaunch(false);

        }
    }

    public void createDefaultData() {
        new ConnectionHistory("test tcp connection","http://google.com",80, DevConsts.TCP_CLIENT).save();
        new ConnectionHistory("test udp connection","127.0.0.1",1055, DevConsts.UDP_CLIENT).save();
        new ConnectionHistory("test ping","http://google.com",80, DevConsts.PING).save();
        new ConnectionHistory("test rest","https://jsonplaceholder.typicode.com/posts",80, DevConsts.REST).save();

    }
}
