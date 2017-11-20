package app.deadmc.devnetworktool.modules;

import java.util.ArrayList;

/**
 * Created by Feren on 25.09.2016.
 */
public class ReceivedMessage {
    private String text;
    private String time;
    private long id;
    private boolean fromServer;

    public ReceivedMessage(String text,String time, long id, boolean fromServer) {
        this.text = text;
        this.time = time;
        this.id = id;
        this.fromServer = fromServer;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public boolean isFromServer() {
        return fromServer;
    }

    public static ArrayList<ReceivedMessage> createReceivedMessageList(int num) {
        ArrayList<ReceivedMessage> arrayList = new ArrayList<ReceivedMessage>();

        for (int i = 1; i <= num; i++) {
            arrayList.add(new ReceivedMessage("","",0, false));
        }

        return arrayList;
    }



}
