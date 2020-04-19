package com.drofff.palindrome.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import static com.drofff.palindrome.utils.StringUtils.randomString;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

@Document
public class AuthenticationToken {

    @Id
    private String id;

    private String value;

    private String userId;

    private LocalDateTime expiresAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        validateNotNull(expiresAt, "Expires at value should not be null");
        LocalDateTime now = LocalDateTime.now();
        return expiresAt.isBefore(now);
    }

    public static class Builder {

        private final AuthenticationToken authenticationToken = new AuthenticationToken();

        public Builder forUser(User user) {
            authenticationToken.userId = user.getId();
            return this;
        }

        public Builder expiresAt(LocalDateTime dateTime) {
            authenticationToken.expiresAt = dateTime;
            return this;
        }

        public AuthenticationToken build() {
            authenticationToken.value = randomString();
            return authenticationToken;
        }

    }

}