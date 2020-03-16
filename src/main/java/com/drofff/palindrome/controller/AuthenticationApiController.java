package com.drofff.palindrome.controller;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.*;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.service.AuthenticationService;
import com.drofff.palindrome.service.AuthorizationService;
import com.drofff.palindrome.service.PoliceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;

@RestController
@RequestMapping("/api")
public class AuthenticationApiController {

    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;
    private final PoliceService policeService;

    @Autowired
    public AuthenticationApiController(AuthenticationService authenticationService, AuthorizationService authorizationService,
                                       PoliceService policeService) {
        this.authenticationService = authenticationService;
        this.authorizationService = authorizationService;
        this.policeService = policeService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<RestResponseDto> authenticate(@RequestBody UserDto userDto) {
        try {
            User user = authenticationService.authenticateUser(userDto.getUsername(), userDto.getPassword());
            String token = authorizationService.generateAuthorizationTokenForUser(user);
            RestAuthorizationTokenDto restAuthorizationTokenDto = new RestAuthorizationTokenDto(token);
            return ResponseEntity.ok(restAuthorizationTokenDto);
        } catch (ValidationException e) {
            RestValidationDto restValidationDto = RestValidationDto.fromValidationException(e);
            return ResponseEntity.badRequest()
                    .body(restValidationDto);
        }
    }

    @GetMapping("/police-info")
    public ResponseEntity<RestResponseDto> getCurrentPolice() {
        try {
            User user = getCurrentUser();
            Police police = policeService.getPoliceByUserId(user.getId());
            RestPoliceDto restPoliceDto = new RestPoliceDto(police);
            return ResponseEntity.ok(restPoliceDto);
        } catch (ValidationException e) {
            RestValidationDto restValidationDto = RestValidationDto.fromValidationException(e);
            return ResponseEntity.badRequest()
                    .body(restValidationDto);
        }
    }

}