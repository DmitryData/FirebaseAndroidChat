package com.firebase_info.firebaseandroidchat;

/**
 * Created by Lenovo on 19.02.2018.
 */

public class Message {

    private String text;

    public Message(){

    }

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
