package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.UserApp;

import java.util.Optional;

public interface UserAppService {

    Optional<UserApp> getUserAppByUserIdIfPresent(String userId);

    void createUserApp(UserApp userApp);

    void updateUserApp(UserApp userApp);

    UserApp getUserAppById(String id);

    String generateUserAppAuthorizationTokenForUser(User user);

}
