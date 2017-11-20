package app.deadmc.devnetworktool.modules;

import java.util.ArrayList;

/**
 * Created by Feren on 24.09.2016.
 */
public class JsonInput {
    private String key;
    private String value;

    public JsonInput(String key,String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }



    private static int lastContactId = 0;

    public static ArrayList<JsonInput> createJsonInputsList(int num) {
        ArrayList<JsonInput> contacts = new ArrayList<JsonInput>();

        for (int i = 1; i <= num; i++) {
            contacts.add(new JsonInput("",""));
        }

        return contacts;
    }
}
