package com.drofff.palindrome.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class AuthorizationToken {

    @Id
    private String id;

    private String token;

    private LocalDateTime dueDateTime;

    private String userId;

    public AuthorizationToken() {}

    public AuthorizationToken(String token, String userId, LocalDateTime dueDateTime) {
        this.token = token;
        this.userId = userId;
        this.dueDateTime = dueDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
