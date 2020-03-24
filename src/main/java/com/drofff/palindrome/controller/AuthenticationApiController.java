package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.EndpointConstants.AUTHENTICATE_API_ENDPOINT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.RestAuthorizationTokenDto;
import com.drofff.palindrome.dto.RestResponseDto;
import com.drofff.palindrome.dto.RestValidationDto;
import com.drofff.palindrome.dto.UserDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.service.AuthenticationService;
import com.drofff.palindrome.service.AuthorizationService;

@RestController
public class AuthenticationApiController {

    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;

    @Autowired
    public AuthenticationApiController(AuthenticationService authenticationService, AuthorizationService authorizationService) {
        this.authenticationService = authenticationService;
        this.authorizationService = authorizationService;
    }

    @PostMapping(AUTHENTICATE_API_ENDPOINT)
    public ResponseEntity<RestResponseDto> authenticate(@RequestBody UserDto userDto) {
        try {
            User user = authenticationService.authenticateUser(userDto.getUsername(), userDto.getPassword());
            String token = authorizationService.generateTokenForUser(user);
            RestAuthorizationTokenDto restAuthorizationTokenDto = new RestAuthorizationTokenDto(token);
            return ResponseEntity.ok(restAuthorizationTokenDto);
        } catch (ValidationException e) {
            RestValidationDto restValidationDto = RestValidationDto.fromValidationException(e);
            return ResponseEntity.badRequest()
                    .body(restValidationDto);
        }
    }

}