package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.enums.AccessLevel;

public interface AuthorizationTokenService {

    String generateAuthorizationTokenForUser(User user);

    String generateAuthorizationTokenOfAccessLevelForUser(AccessLevel accessLevel, User user);

    User getUserByAuthorizationToken(String token);

    AccessLevel getAccessLevelOfAuthorizationToken(String token);

}