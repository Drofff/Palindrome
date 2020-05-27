package com.drofff.palindrome.controller.api;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.*;
import com.drofff.palindrome.service.ApiAuthenticationService;
import com.drofff.palindrome.service.AuthenticationTokenService;
import com.drofff.palindrome.service.AuthorizationTokenService;
import com.drofff.palindrome.service.PoliceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.drofff.palindrome.constants.EndpointConstants.*;

@RestController
public class AuthenticationApiController {

    private final ApiAuthenticationService apiAuthenticationService;
    private final AuthorizationTokenService authorizationTokenService;
    private final AuthenticationTokenService authenticationTokenService;
    private final PoliceService policeService;

    @Autowired
    public AuthenticationApiController(ApiAuthenticationService apiAuthenticationService, AuthorizationTokenService authorizationTokenService,
                                       AuthenticationTokenService authenticationTokenService, PoliceService policeService) {
        this.apiAuthenticationService = apiAuthenticationService;
        this.authorizationTokenService = authorizationTokenService;
        this.authenticationTokenService = authenticationTokenService;
        this.policeService = policeService;
    }

    @PostMapping(AUTHENTICATE_API_ENDPOINT)
    public ResponseEntity<RestResponseDto> authenticate(@RequestBody UserDto userDto) {
        User user = apiAuthenticationService.authenticateUserByCredentials(userDto.getUsername(), userDto.getPassword());
        if(hasTwoStepAuthEnabled(user)) {
            apiAuthenticationService.requestTwoStepAuthForUser(user);
            RestTwoStepAuthRequestDto restTwoStepAuthRequestDto = twoStepAuthRequestForUser(user);
            return ResponseEntity.ok(restTwoStepAuthRequestDto);
        }
        return restAuthorizationForUser(user);
    }

    private boolean hasTwoStepAuthEnabled(User user) {
        Police police = policeService.getPoliceByUserId(user.getId());
        return police.isTwoStepAuthEnabled();
    }

    private RestTwoStepAuthRequestDto twoStepAuthRequestForUser(User user) {
        RestTwoStepAuthRequestDto restTwoStepAuthRequestDto = new RestTwoStepAuthRequestDto();
        restTwoStepAuthRequestDto.setMessage("Use a code sent to you by email to verify your identity");
        restTwoStepAuthRequestDto.setUserId(user.getId());
        return restTwoStepAuthRequestDto;
    }

    @PostMapping(TWO_STEP_AUTH_API_ENDPOINT)
    public ResponseEntity<RestResponseDto> completeTwoStepAuth(@RequestBody RestTwoStepAuthDto restTwoStepAuthDto) {
        String userId = restTwoStepAuthDto.getUserId();
        String token = restTwoStepAuthDto.getToken();
        User user = apiAuthenticationService.completeTwoStepAuthForUserWithId(userId, token);
        return restAuthorizationForUser(user);
    }

    private ResponseEntity<RestResponseDto> restAuthorizationForUser(User user) {
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