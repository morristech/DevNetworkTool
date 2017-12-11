package app.deadmc.devnetworktool.models;

public class SimpleString {

    String text;
    boolean bold;

    public SimpleString(String text) {
        this.text = text;
        this.bold = false;
    }

    public SimpleString(String text,boolean bold) {
        this.text = text;
        this.bold = bold;
    }

    public String getText() {
        return text;
    }

    public boolean isBold() {
        return bold;
    }


}
