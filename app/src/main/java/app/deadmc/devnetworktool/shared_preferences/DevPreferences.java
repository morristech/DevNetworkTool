package app.deadmc.devnetworktool.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class DevPreferences {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor preferencesEditor;
    public static final String SHARED_PREFERENCES_NAME = "DevNetworkToolPreferences";

    public static final String PREFERENCES_FIRST_LAUNCH = "firstLaunch";
    public static final String PREFERENCES_PING_DELAY = "pingDelay";

    /**
     * required to call init in Application class
     * @param context
     */
    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * provides information about is it first launch or not
     * @return true if first launch, false if is not
     */
    public static boolean getFirstLaunch() {
        return sharedPreferences.getBoolean(PREFERENCES_FIRST_LAUNCH, true);
    }

    public static void setFirstLaunch(Boolean value) {
        preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putBoolean(PREFERENCES_FIRST_LAUNCH, value);
        preferencesEditor.apply();
    }

    /**
     * provides information about currently set delay for ping requests
     * @return delay for ping requests in milliseconds
     */
    public static int getPingDelay() {
        return sharedPreferences.getInt(PREFERENCES_PING_DELAY, 1000);
    }

    public static void setPingDelay(int value) {
        preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putInt(PREFERENCES_PING_DELAY, value);
        preferencesEditor.apply();
    }
}
