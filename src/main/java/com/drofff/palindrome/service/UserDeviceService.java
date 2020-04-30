package com.drofff.palindrome.service;

import com.drofff.palindrome.document.UserDevice;

import java.util.List;

public interface UserDeviceService {

    void registerDevice(UserDevice userDevice);

    void updateDeviceRegistrationToken(String macAddress, String registrationToken);

    List<UserDevice> getActiveDevices();

    void requestExternalAuthThroughDeviceUsingToken(UserDevice device, String token);

    UserDevice getUserDeviceById(String id);

}