package com.drofff.palindrome.type;

import com.drofff.palindrome.document.UserDevice;

public class ExternalAuthenticationMessage {

    private static final String MESSAGE_TYPE = "two-step-auth-request";

    private String token;

    private String optionId;

    private String macAddress;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMessageType() {
        return MESSAGE_TYPE;
    }

    public static class Builder {

        private final ExternalAuthenticationMessage authenticationMessage = new ExternalAuthenticationMessage();

        public Builder forDevice(UserDevice device) {
            authenticationMessage.optionId = device.getId();
            authenticationMessage.macAddress = device.getMacAddress();
            return this;
        }

        public Builder withToken(String token) {
            authenticationMessage.token = token;
            return this;
        }

        public ExternalAuthenticationMessage build() {
            return authenticationMessage;
        }

    }

}
