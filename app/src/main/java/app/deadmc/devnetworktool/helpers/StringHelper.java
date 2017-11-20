package app.deadmc.devnetworktool.helpers;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by Feren on 25.09.2016.
 */
public class StringHelper {

    public static String formatString(String text) {

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append("\n" + indentString + letter + "\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n" + indentString + letter);
                    break;
                case ',':
                    json.append(letter + "\n" + indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }

    public static float getPingFromString(String rawString) {
        rawString = rawString.trim().replace(" ", "");
        int index = rawString.lastIndexOf("time=");
        int lastIndex = rawString.lastIndexOf("ms---");
        float value = 0f;
        try {
            String result = rawString.substring(index + 5, lastIndex);
            value = Float.parseFloat(result);
        } catch (Exception e) {

        }
        return value;

    }

    public static int getTtlFromString(String rawString) {
        rawString = rawString.trim().replace(" ", "");
        int index = rawString.lastIndexOf("ttl=");
        int lastIndex = rawString.lastIndexOf("time=");
        int value = 0;
        //Log.e("ping1", pingString);
        //Log.e("ping2", "lastIndex ="+ lastIndex);
        try {
            String result = rawString.substring(index + 4, lastIndex);
            value = Integer.parseInt(result);
        } catch (Exception e) {

        }
        return value;

    }

    public static String getIpAddressFromString(String rawString) {
        rawString = rawString.trim().replace(" ", "");
        int index = rawString.indexOf("(");
        int lastIndex = rawString.indexOf(")");
        //Log.e("ping1", pingString);
        //Log.e("ping2", "lastIndex ="+ lastIndex);
        String result = "";
        try {
            result = rawString.substring(index+1, lastIndex);
        } catch (Exception e) {

        }

        return result;

    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static int portFromString(String portString) {
        int port = 80;
        if (!portString.isEmpty()) {
            try {
                port = Integer.parseInt(portString.toString());
            } catch (Exception e) {}
        }
        return port;
    }
}
