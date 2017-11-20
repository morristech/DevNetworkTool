package app.deadmc.devnetworktool.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.deadmc.devnetworktool.singletons.SharedData;

/**
 * Created by Feren on 25.09.2016.
 */
public class DateTimeHelper {

    public static DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    public static DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    public static DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    public static String getCurrentTime() {

        Date date = new Date();
        return TIME_FORMAT.format(date);
    }

    public static String getTimeFromTimestamp(long timestamp) {
        //Log.e("timestamp",""+timestamp);
        Date date = new Date(timestamp);
        return TIME_FORMAT.format(date);
    }

    public static String getDateTimeFromTimestamp(long timestamp) {
        //Log.e("timestamp",""+timestamp);
        Date date = new Date(timestamp);
        return DATE_TIME_FORMAT.format(date);
    }

    public static String getDateFromTimestamp(long timestamp) {
        //Log.e("timestamp",""+timestamp);
        Date date = new Date(timestamp);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(SharedData.getInstance().getContext());
        return dateFormat.format(date);
    }

    public static String getDateAndTime(Long timestamp) {
        String date = DateTimeHelper.getDateFromTimestamp(timestamp);
        String time = DateTimeHelper.getTimeFromTimestamp(timestamp);
        //return time+System.getProperty("line.separator")+date;
        return date+" "+time;
    }
}
