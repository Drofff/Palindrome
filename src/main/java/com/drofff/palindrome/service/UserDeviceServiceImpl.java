package com.drofff.palindrome.service;

import com.drofff.palindrome.configuration.properties.FirebaseMessagingProperties;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.UserDevice;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.UserDeviceRepository;
import com.drofff.palindrome.type.ExternalAuthenticationMessage;
import com.drofff.palindrome.type.FirebaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.FirebaseNotificationUtils.getTwoStepAuthNotification;
import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class UserDeviceServiceImpl implements UserDeviceService {

    private static final String AUTHORIZATION_KEY_PREFIX = "key=";

    private final UserDeviceRepository userDeviceRepository;
    private final RestTemplate restTemplate;
    private final FirebaseMessagingProperties firebaseMessagingProperties;

    @Autowired
    public UserDeviceServiceImpl(UserDeviceRepository userDeviceRepository, RestTemplate restTemplate,
                                 FirebaseMessagingProperties firebaseMessagingProperties) {
        this.userDeviceRepository = userDeviceRepository;
        this.restTemplate = restTemplate;
        this.firebaseMessagingProperties = firebaseMessagingProperties;
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
    public void updateDeviceRegistrationToken(String macAddress, String registrationToken) {
        validateNotNull(registrationToken, "Registration token is required");
        UserDevice userDevice = getUserDeviceByMacAddress(macAddress);
        validateIsDeviceOwner(userDevice);
        userDevice.setRegistrationToken(registrationToken);
        userDeviceRepository.save(userDevice);
    }

    private UserDevice getUserDeviceByMacAddress(String macAddress) {
        validateNotNull(macAddress, "MAC address should not be null");
        return userDeviceRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new ValidationException("Device with such MAC address doesn't exist"));
    }

    private void validateIsDeviceOwner(UserDevice device) {
        if(isNotOwnerOfDevice(device)) {
            throw new ValidationException("Invalid permissions");
        }
    }

    private boolean isNotOwnerOfDevice(UserDevice device) {
        return !isOwnerOfDevice(device);
    }

    private boolean isOwnerOfDevice(UserDevice device) {
        String currentUserId = getCurrentUser().getId();
        return currentUserId.equals(device.getUserId());
    }

    @Override
    public List<UserDevice> getActiveDevices() {
        return userDeviceRepository.findAll();
    }

    @Override
    public void requestExternalAuthThroughDeviceUsingToken(UserDevice device, String token) {
        validateNotNull(device, "Device should not be null");
        validateNotNull(token, "Token is required");
        String firebaseMessagingUrl = firebaseMessagingProperties.getUrl();
        HttpEntity<FirebaseMessage> firebaseMessageEntity = buildFirebaseMessageForDeviceWithToken(device, token);
        restTemplate.exchange(firebaseMessagingUrl, POST, firebaseMessageEntity, Object.class);
    }

    private HttpEntity<FirebaseMessage> buildFirebaseMessageForDeviceWithToken(UserDevice device, String token) {
        HttpHeaders messagingHeaders = firebaseMessagingHeaders();
        ExternalAuthenticationMessage authenticationMessage = authenticationMessageForDeviceWithToken(device, token);
        FirebaseMessage firebaseMessage = toFirebaseMessage(authenticationMessage);
        firebaseMessage.setTo(device.getRegistrationToken());
        return new HttpEntity<>(firebaseMessage, messagingHeaders);
    }

    private HttpHeaders firebaseMessagingHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        String authorizationKey = AUTHORIZATION_KEY_PREFIX + firebaseMessagingProperties.getKey();
        httpHeaders.add(AUTHORIZATION, authorizationKey);
        return httpHeaders;
    }

    private ExternalAuthenticationMessage authenticationMessageForDeviceWithToken(UserDevice device, String token) {
        return new ExternalAuthenticationMessage.Builder()
                .forDevice(device)
                .withToken(token)
                .build();
    }

    private FirebaseMessage toFirebaseMessage(ExternalAuthenticationMessage externalAuthenticationMessage) {
        FirebaseMessage firebaseMessage = new FirebaseMessage();
        firebaseMessage.setData(externalAuthenticationMessage);
        firebaseMessage.setNotification(getTwoStepAuthNotification());
        return firebaseMessage;
    }

    @Override
    public UserDevice getUserDeviceById(String id) {
        validateNotNull(id, "User device id is required");
        return userDeviceRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Device with such id doesn't exist"));
    }

}
