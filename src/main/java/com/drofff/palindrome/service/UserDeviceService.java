package com.drofff.palindrome.service;

import com.drofff.palindrome.document.UserDevice;
import com.drofff.palindrome.type.UserDeviceRequest;

import java.util.List;

public interface UserDeviceService {

    void registerDevice(UserDevice userDevice);

    List<UserDevice> getActiveDevices();

    void requestExternalAuthThroughDeviceUsingToken(UserDevice device, String token);

    List<UserDeviceRequest> getRequestsForDeviceWithId(String deviceId);

    UserDevice getUserDeviceById(String id);

}