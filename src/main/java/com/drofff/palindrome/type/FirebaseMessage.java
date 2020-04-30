package com.drofff.palindrome.type;

public class FirebaseMessage {

    private FirebaseNotification notification;

    private ExternalAuthenticationMessage data;

    private String to;

    public FirebaseNotification getNotification() {
        return notification;
    }

    public void setNotification(FirebaseNotification notification) {
        this.notification = notification;
    }

    public ExternalAuthenticationMessage getData() {
        return data;
    }

    public void setData(ExternalAuthenticationMessage data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
