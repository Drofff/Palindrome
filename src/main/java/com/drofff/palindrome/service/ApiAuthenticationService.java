package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;

public interface ApiAuthenticationService {

    User authenticateUserByCredentials(String username, String password);

    void requestTwoStepAuthForUser(User user);

    User completeTwoStepAuthForUserWithId(String userId, String token);

}