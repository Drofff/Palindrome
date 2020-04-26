package com.drofff.palindrome.cache;

import com.drofff.palindrome.document.UserDevice;
import com.drofff.palindrome.type.UserDeviceRequest;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Optional;

import static java.util.concurrent.TimeUnit.MINUTES;

public class UserDeviceRequestCache {

    private static final Cache<String, UserDeviceRequest> USER_DEVICE_REQUEST_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(5, MINUTES)
            .build();

    private UserDeviceRequestCache() {}

    public static void saveRequestForUserDevice(UserDeviceRequest request, UserDevice device) {
        USER_DEVICE_REQUEST_CACHE.put(device.getId(), request);
    }

    public static Optional<UserDeviceRequest> getRequestForUserDeviceIfPresent(UserDevice device) {
        UserDeviceRequest request = USER_DEVICE_REQUEST_CACHE.getIfPresent(device.getId());
        return Optional.ofNullable(request);
    }

}
