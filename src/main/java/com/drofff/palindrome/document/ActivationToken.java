package com.drofff.palindrome.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.drofff.palindrome.utils.StringUtils.randomString;

@Document
public class ActivationToken {

    @Id
    private String id;

    private String value;

    private String userId;

    public static ActivationToken forUser(User user) {
        ActivationToken activationToken = new ActivationToken();
        activationToken.userId = user.getId();
        activationToken.value = randomString();
        return activationToken;
    }

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

}
