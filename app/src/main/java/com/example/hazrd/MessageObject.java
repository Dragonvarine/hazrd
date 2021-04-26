package com.example.hazrd;

public class MessageObject {
    String userID;
    String username;
    String text;

    MessageObject(String userID, String username, String text) {
        this.userID = userID;
        this.username = username;
        this.text = text;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }
}
