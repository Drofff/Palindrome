package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.UserApp;
import com.drofff.palindrome.dto.UserAppDto;
import com.drofff.palindrome.dto.UserDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.UserAppDtoMapper;
import com.drofff.palindrome.service.ApiAuthenticationService;
import com.drofff.palindrome.service.UserAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.drofff.palindrome.constants.EndpointConstants.RELATIVE_HOME_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.USER_APP_ENDPOINTS_BASE;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.TOKEN_PARAM;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.FormattingUtils.concatPathSegments;
import static com.drofff.palindrome.utils.FormattingUtils.uriWithQueryParams;
import static com.drofff.palindrome.utils.ModelUtils.*;
import static java.util.Collections.singletonList;

@Controller
@RequestMapping(USER_APP_ENDPOINTS_BASE)
public class UserAppAuthenticationController {

    private static final String USER_APP_HOME_VIEW = "userAppHomePage";

    private static final String USER_APP_PARAM = "userApp";

    private final UserAppService userAppService;
    private final ApiAuthenticationService apiAuthenticationService;
    private final UserAppDtoMapper userAppDtoMapper;

    @Autowired
    public UserAppAuthenticationController(UserAppService userAppService, ApiAuthenticationService apiAuthenticationService,
                                           UserAppDtoMapper userAppDtoMapper) {
        this.userAppService = userAppService;
        this.apiAuthenticationService = apiAuthenticationService;
        this.userAppDtoMapper = userAppDtoMapper;
    }

    @GetMapping(RELATIVE_HOME_ENDPOINT)
    @PreAuthorize("hasAuthority('APP')")
    public String getUserAppHomePage(Model model) {
        User currentUser = getCurrentUser();
        userAppService.getUserAppByUserIdIfPresent(currentUser.getId())
                .ifPresent(userApp -> model.addAttribute(USER_APP_PARAM, userApp));
        return USER_APP_HOME_VIEW;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('APP')")
    public String createUserApp(UserAppDto userAppDto, Model model) {
        UserApp userApp = userAppDtoMapper.toEntity(userAppDto);
        try {
            userAppService.createUserApp(userApp);
            model.addAttribute(MESSAGE_PARAM, "Successfully created user app data");
            model.addAttribute(USER_APP_PARAM, userApp);
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(USER_APP_PARAM, userApp);
        }
        return USER_APP_HOME_VIEW;
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('APP')")
    public String updateUserApp(@PathVariable String id, UserAppDto userAppDto, Model model) {
        UserApp userApp = userAppDtoMapper.toEntity(userAppDto);
        userApp.setId(id);
        try {
            userAppService.updateUserApp(userApp);
            model.addAttribute(MESSAGE_PARAM, "Successfully updated user app data");
            model.addAttribute(USER_APP_PARAM, userApp);
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(USER_APP_PARAM, userApp);
        }
        return USER_APP_HOME_VIEW;
    }

    @GetMapping("/{appId}/authenticate")
    public String getAppAuthenticationPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
                                           @PathVariable String appId, Model model) {
        UserApp userApp = userAppService.getUserAppById(appId);
        model.addAttribute(USER_APP_PARAM, userApp);
        model.addAttribute(MESSAGE_PARAM, message);
        return "appAuthenticationPage";
    }

    @PostMapping("/{appId}/authenticate")
    public String authenticateUserForAppWithId(@PathVariable String appId, UserDto userDto) {
        try {
            User user = apiAuthenticationService
                    .authenticateUserByCredentials(userDto.getUsername(), userDto.getPassword());
            UserApp userApp = userAppService.getUserAppById(appId);
            String appAuthToken = userAppService.generateUserAppAuthorizationTokenForUser(user);
            String redirectUrl = urlToUserAppWithToken(userApp, appAuthToken);
            return redirectTo(redirectUrl);
        } catch(ValidationException e) {
            String authUri = "/user-app/" + appId + "/authenticate";
            return redirectToWithMessage(authUri, e.getMessage());
        }
    }

    private String urlToUserAppWithToken(UserApp userApp, String token) {
        String url = concatPathSegments(userApp.getBaseUrl(), userApp.getRedirectUri());
        Pair<String, String> tokenParam = Pair.of(TOKEN_PARAM, token);
        return uriWithQueryParams(url, singletonList(tokenParam));
    }

}