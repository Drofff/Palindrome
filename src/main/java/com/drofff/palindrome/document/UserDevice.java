package com.drofff.palindrome.document;

import com.drofff.palindrome.type.ExternalAuthenticationOption;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Document
public class UserDevice implements Entity {

    @Id
    private String id;

    @NotBlank(message = "Label is required")
    private String label;

    @NotBlank(message = "Mac Address should be provided")
    @Pattern(regexp = "^[0-9a-fA-F]{1,2}(:[0-9a-fA-F]{1,2}){5}$", message = "Mac Address is of incorrect format")
    private String macAddress;

    @NotBlank(message = "Registration token is required")
    private String registrationToken;

    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ExternalAuthenticationOption toAuthOption() {
        return new ExternalAuthenticationOption.Builder()
                .usingPushNotification()
                .withId(id)
                .withLabel(label)
                .build();
    }

}
