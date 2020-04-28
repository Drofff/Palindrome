package com.drofff.palindrome.controller;

import com.drofff.palindrome.document.UserDevice;
import com.drofff.palindrome.dto.*;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.UserDeviceDtoMapper;
import com.drofff.palindrome.service.UserDeviceService;
import com.drofff.palindrome.type.UserDeviceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        try {
            userDeviceService.registerDevice(userDevice);
            RestMessageDto restMessageDto = new RestMessageDto("Device has been successfully registered");
            return ResponseEntity.ok(restMessageDto);
        } catch(ValidationException e) {
            RestValidationDto restValidationDto = RestValidationDto.fromValidationException(e);
            return ResponseEntity.badRequest()
                    .body(restValidationDto);
        }
    }

    @GetMapping("/requests")
    public ResponseEntity<RestResponseDto> getRequestsForDevice(String macAddress) {
        try {
            List<UserDeviceRequest> requests = userDeviceService.getRequestsForDeviceWithMacAddress(macAddress);
            RestListDto<UserDeviceRequest> requestsListDto = new RestListDto<>(requests);
            return ResponseEntity.ok(requestsListDto);
        } catch(ValidationException e) {
            RestValidationDto restValidationDto = RestValidationDto.fromValidationException(e);
            return ResponseEntity.badRequest()
                    .body(restValidationDto);
        }
    }

}
