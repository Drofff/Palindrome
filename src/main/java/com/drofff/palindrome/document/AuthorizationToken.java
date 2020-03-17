package com.drofff.palindrome.document;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AuthorizationToken {

    @Id
    private String userId;

    private String token;

    private LocalDateTime dueDateTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

    public static class Builder {

    	private AuthorizationToken authorizationToken = new AuthorizationToken();

    	public Builder forUser(User user) {
    		authorizationToken.userId = user.getId();
    		return this;
	    }

	    public Builder dueDateTime(LocalDateTime dateTime) {
    		authorizationToken.dueDateTime = dateTime;
    		return this;
	    }

	    public AuthorizationToken build() {
    		authorizationToken.token = UUID.randomUUID().toString();
    		return authorizationToken;
	    }

    }

}
