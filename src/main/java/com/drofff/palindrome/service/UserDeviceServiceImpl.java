package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.UserDevice;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.UserDeviceRepository;
import com.drofff.palindrome.type.UserDeviceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.drofff.palindrome.cache.UserDeviceRequestCache.getRequestForUserDeviceIfPresent;
import static com.drofff.palindrome.cache.UserDeviceRequestCache.saveRequestForUserDevice;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static java.util.Collections.emptyList;

@Service
public class UserDeviceServiceImpl implements UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;

    @Autowired
    public UserDeviceServiceImpl(UserDeviceRepository userDeviceRepository) {
        this.userDeviceRepository = userDeviceRepository;
    }

    @Override
    public void registerDevice(UserDevice userDevice) {
        validateNotNull(userDevice);
        validate(userDevice);
        validateMacAddressIsUnique(userDevice.getMacAddress());
        User currentUser = getCurrentUser();
        userDevice.setUserId(currentUser.getId());
        userDeviceRepository.save(userDevice);
    }

    private void validateMacAddressIsUnique(String macAddress) {
        if(existsDeviceWithMacAddress(macAddress)) {
            throw new ValidationException("Device with such MAC address already exists");
        }
    }

    private boolean existsDeviceWithMacAddress(String macAddress) {
        return userDeviceRepository.findByMacAddress(macAddress).isPresent();
    }

    @Override
    public List<UserDevice> getActiveDevices() {
        return userDeviceRepository.findAll();
    }

    @Override
    public void requestExternalAuthThroughDeviceUsingToken(UserDevice device, String token) {
        validateNotNull(device, "Device should not be null");
        validateNotNull(token, "Token is required");
        UserDeviceRequest request = UserDeviceRequest.forDeviceWithToken(device, token);
        saveRequestForUserDevice(request, device);
    }

    @Override
    public List<UserDeviceRequest> getRequestsForDeviceWithMacAddress(String macAddress) {
        UserDevice userDevice = getUserDeviceByMacAddress(macAddress);
        return getRequestForUserDeviceIfPresent(userDevice)
                .map(Collections::singletonList)
                .orElse(emptyList());
    }

    private UserDevice getUserDeviceByMacAddress(String macAddress) {
        validateNotNull(macAddress, "MAC Address is required");
        return userDeviceRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new ValidationException("Device with such MAC Address doesn't exist"));
    }

    @Override
    public UserDevice getUserDeviceById(String id) {
        validateNotNull(id, "User device id is required");
        return userDeviceRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Device with such id doesn't exist"));
    }

}
