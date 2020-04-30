package com.drofff.palindrome.controller.api;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.RefreshTokenDto;
import com.drofff.palindrome.dto.RestAuthorizationDto;
import com.drofff.palindrome.dto.RestResponseDto;
import com.drofff.palindrome.dto.UserDto;
import com.drofff.palindrome.service.AuthenticationService;
import com.drofff.palindrome.service.AuthenticationTokenService;
import com.drofff.palindrome.service.AuthorizationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.drofff.palindrome.constants.EndpointConstants.AUTHENTICATE_API_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.REFRESH_TOKEN_API_ENDPOINT;

@RestController
public class AuthenticationApiController {

    private final AuthenticationService authenticationService;
    private final AuthorizationTokenService authorizationTokenService;
    private final AuthenticationTokenService authenticationTokenService;

    @Autowired
    public AuthenticationApiController(AuthenticationService authenticationService, AuthorizationTokenService authorizationTokenService,
                                       AuthenticationTokenService authenticationTokenService) {
        this.authenticationService = authenticationService;
        this.authorizationTokenService = authorizationTokenService;
        this.authenticationTokenService = authenticationTokenService;
    }

    @PostMapping(AUTHENTICATE_API_ENDPOINT)
    public ResponseEntity<RestResponseDto> authenticate(@RequestBody UserDto userDto) {
        User user = authenticationService.authenticateUserByCredentials(userDto.getUsername(), userDto.getPassword());
        String authenticationToken = authenticationTokenService.generateAuthenticationTokenForUser(user);
        String authorizationToken = authorizationTokenService.generateAuthorizationTokenForUser(user);
        return new RestAuthorizationDto.Builder().forUser(user)
                .withAuthorizationToken(authorizationToken)
                .withAuthenticationToken(authenticationToken)
                .asResponseEntityOk();
    }

    @PostMapping(REFRESH_TOKEN_API_ENDPOINT)
	public ResponseEntity<RestResponseDto> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        String authenticationToken = refreshTokenDto.getAuthenticationToken();
        String userId = refreshTokenDto.getUserId();
        User user = authenticationTokenService.authenticateUserWithIdByToken(userId, authenticationToken);
        String authorizationToken = authorizationTokenService.generateAuthorizationTokenForUser(user);
        return new RestAuthorizationDto.Builder().forUser(user)
                .withAuthorizationToken(authorizationToken)
                .withAuthenticationToken(authenticationToken)
                .asResponseEntityOk();
    }

}