package com.drofff.palindrome.type;

import org.json.simple.JSONObject;

public class FirebaseNotification {

    private String title;

    private String body;

    public static FirebaseNotification fromJSONObject(JSONObject jsonObject) {
        String title = (String) jsonObject.get("title");
        String body = (String) jsonObject.get("body");
        return new FirebaseNotification(title, body);
    }

    private FirebaseNotification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
