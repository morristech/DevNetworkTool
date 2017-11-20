package app.deadmc.devnetworktool.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.ByteArrayInputStream;
import java.util.regex.Pattern;

/**
 * Created by Feren on 25.04.2017.
 */

public class FileFormatHelper {
    public static final int UNDEFINED = 0;
    public static final int JSON = 1;
    public static final int XML = 2;
    public static final int HTML = 3;

    public static int getTypeOfString(String value) {
        if (isJson(value))
            return JSON;

        if (isXml(value))
            return JSON;

        if (isHtml(value))
            return HTML;
        return UNDEFINED;
    }

    public static boolean isHtml(String value) {
        Pattern htmlPattern = Pattern.compile(".*\\<[^>]+>.*", Pattern.DOTALL);
        return htmlPattern.matcher(value).matches();
    }

    public static boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException ex) {
            try {
                new JSONArray(value);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isXml(String value) {
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(new DefaultHandler());
            InputSource source = new InputSource(new ByteArrayInputStream(value.getBytes()));
            parser.parse(source);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
