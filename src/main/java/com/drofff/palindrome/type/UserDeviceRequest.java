package com.drofff.palindrome.type;

import com.drofff.palindrome.document.UserDevice;

public class UserDeviceRequest {

    private String token;

    private String optionId;

    public static UserDeviceRequest forDeviceWithToken(UserDevice device, String token) {
        return new UserDeviceRequest(token, device.getId());
    }

    public UserDeviceRequest(String token, String optionId) {
        this.token = token;
        this.optionId = optionId;
    }

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

}
