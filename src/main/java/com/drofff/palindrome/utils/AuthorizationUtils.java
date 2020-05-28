package com.drofff.palindrome.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.drofff.palindrome.constants.EndpointConstants.*;
import static com.drofff.palindrome.constants.RegexConstants.ANY_SYMBOL;
import static com.drofff.palindrome.utils.StringUtils.removePartFromStr;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static java.util.Arrays.asList;

public class AuthorizationUtils {

    private static final List<String> OPEN_API_ENDPOINT_PATTERNS = asList(AUTHENTICATE_API_ENDPOINT,
            REFRESH_TOKEN_API_ENDPOINT, TWO_STEP_AUTH_API_ENDPOINT, "/api/drivers/.*/photo",
            "/api/polices/.*/photo", API_RESOURCE_ENDPOINTS_BASE + ANY_SYMBOL);

    private static final String AUTHORIZATION_TOKEN_HEADER = "Authorization";
    private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";

    private AuthorizationUtils() {}

    public static String getAuthorizationTokenFromHeader(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(AUTHORIZATION_TOKEN_HEADER);
        validateNotNull(token, "Missing authorization token");
        return removeTokenPrefix(token);
    }

    private static String removeTokenPrefix(String token) {
        return removePartFromStr(AUTHORIZATION_TOKEN_PREFIX, token);
    }

    public static boolean isOpenEndpoint(String uri) {
        return OPEN_API_ENDPOINT_PATTERNS.stream()
                .anyMatch(uri::matches);
    }

}
