package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;

public interface AuthorizationService {

    String generateTokenForUser(User user);

    User getUserByToken(String token);

}