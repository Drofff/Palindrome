package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.UserApp;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.UserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.drofff.palindrome.enums.AccessLevel.READ_ONLY;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ValidationUtils.*;

@Service
public class UserAppServiceImpl implements UserAppService {

    private final UserAppRepository userAppRepository;
    private final AuthorizationTokenService authorizationTokenService;

    @Autowired
    public UserAppServiceImpl(UserAppRepository userAppRepository, AuthorizationTokenService authorizationTokenService) {
        this.userAppRepository = userAppRepository;
        this.authorizationTokenService = authorizationTokenService;
    }

    @Override
    public Optional<UserApp> getUserAppByUserIdIfPresent(String userId) {
        validateNotNull(userId, "User id is required");
        return userAppRepository.findByUserId(userId);
    }

    @Override
    public void createUserApp(UserApp userApp) {
        User currentUser = getCurrentUser();
        validateHasNoUserApp(currentUser);
        validate(userApp);
        userApp.setUserId(currentUser.getId());
        userAppRepository.save(userApp);
    }

    private void validateHasNoUserApp(User user) {
        if(existsUserAppWithUserId(user.getId())) {
            throw new ValidationException("User has already obtained an app");
        }
    }

    private boolean existsUserAppWithUserId(String userId) {
        return userAppRepository.findByUserId(userId).isPresent();
    }

    @Override
    public void updateUserApp(UserApp userApp) {
        validateNotNullEntityHasId(userApp);
        validate(userApp);
        User currentUser = getCurrentUser();
        validateIsOwnerOfAppWithId(currentUser, userApp.getId());
        userApp.setUserId(currentUser.getId());
        userAppRepository.save(userApp);
    }

    private void validateIsOwnerOfAppWithId(User user, String id) {
        if(isNotOwnerOfAppWithId(user, id)) {
            throw new ValidationException("User should be an app owner");
        }
    }

    private boolean isNotOwnerOfAppWithId(User user, String id) {
        return !isOwnerOfAppWithId(user, id);
    }

    private boolean isOwnerOfAppWithId(User user, String id) {
        UserApp userApp = getUserAppById(id);
        return userApp.getUserId().equals(user.getId());
    }

    @Override
    public UserApp getUserAppById(String id) {
        validateNotNull(id, "User App id is required");
        return userAppRepository.findById(id)
                .orElseThrow(() -> new ValidationException("User App with such id doesn't exist"));
    }

    @Override
    public String generateUserAppAuthorizationTokenForUser(User user) {
        validateNotNullEntityHasId(user);
        return authorizationTokenService.generateAuthorizationTokenOfAccessLevelForUser(READ_ONLY, user);
    }

}