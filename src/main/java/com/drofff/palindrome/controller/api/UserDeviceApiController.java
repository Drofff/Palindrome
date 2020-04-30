package com.drofff.palindrome.controller.api;

import com.drofff.palindrome.document.UserDevice;
import com.drofff.palindrome.dto.RestMessageDto;
import com.drofff.palindrome.dto.RestResponseDto;
import com.drofff.palindrome.dto.UserDeviceDto;
import com.drofff.palindrome.mapper.UserDeviceDtoMapper;
import com.drofff.palindrome.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-devices")
public class UserDeviceApiController {

    private final UserDeviceService userDeviceService;
    private final UserDeviceDtoMapper userDeviceDtoMapper;

    @Autowired
    public UserDeviceApiController(UserDeviceService userDeviceService, UserDeviceDtoMapper userDeviceDtoMapper) {
        this.userDeviceService = userDeviceService;
        this.userDeviceDtoMapper = userDeviceDtoMapper;
    }

    @PostMapping
    public ResponseEntity<RestResponseDto> registerDevice(@RequestBody UserDeviceDto userDeviceDto) {
        UserDevice userDevice = userDeviceDtoMapper.toEntity(userDeviceDto);
        userDeviceService.registerDevice(userDevice);
        RestMessageDto restMessageDto = new RestMessageDto("Device has been successfully registered");
        return ResponseEntity.ok(restMessageDto);
    }

    @PostMapping("/{macAddress}/refresh-registration-token")
    public ResponseEntity<RestResponseDto> refreshDeviceRegistrationToken(@PathVariable String macAddress, String registrationToken) {
        userDeviceService.updateDeviceRegistrationToken(macAddress, registrationToken);
        RestMessageDto restMessageDto = new RestMessageDto("Successfully updated device's registration token");
        return ResponseEntity.ok(restMessageDto);
    }

}
