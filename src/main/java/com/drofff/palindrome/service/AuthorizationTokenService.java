package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;

public interface AuthorizationTokenService {

    String generateAuthorizationTokenForUser(User user);

    User getUserByAuthorizationToken(String token);

}