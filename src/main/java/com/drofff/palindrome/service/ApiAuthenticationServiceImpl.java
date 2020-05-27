package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.type.Mail;
import com.drofff.palindrome.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.drofff.palindrome.cache.TokenCache.*;
import static com.drofff.palindrome.constants.MessageConstants.INVALID_TOKEN_MESSAGE;
import static com.drofff.palindrome.utils.ValidationUtils.*;
import static java.util.stream.IntStream.range;

@Service
public class ApiAuthenticationServiceImpl implements ApiAuthenticationService {

    private static final Random RANDOM = new Random();

    private static final int TWO_STEP_AUTH_TOKEN_LENGTH = 6;

    private final UserService userService;
    private final MailService mailService;

    @Autowired
    public ApiAuthenticationServiceImpl(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @Override
    public User authenticateUserByCredentials(String username, String password) {
        User user = userService.getUserByUsername(username);
        userService.validateIsPasswordOfUser(password, user);
        return user;
    }

    @Override
    public void requestTwoStepAuthForUser(User user) {
        validateNotNullEntityHasId(user);
        String token = generateTwoStepAuthToken();
        Mail twoStepAuthMail = MailUtils.getRestTwoStepAuthMail(token);
        mailService.sendMailTo(twoStepAuthMail, user.getUsername());
        doOnTokenReceive(token, (userId, params) -> {
            validateAreEqual(user.getId(), userId, INVALID_TOKEN_MESSAGE);
            removeTokenListener(token);
        });
    }

    private String generateTwoStepAuthToken() {
        StringBuilder tokenBuilder = new StringBuilder();
        range(0, TWO_STEP_AUTH_TOKEN_LENGTH)
                .forEach(pos -> tokenBuilder.append(randomPositiveNumber()));
        return tokenBuilder.toString();
    }

    private int randomPositiveNumber() {
        int randomNumber = RANDOM.nextInt() % 10;
        return randomNumber < 0 ? randomNumber + 10 : randomNumber;
    }

    @Override
    public User completeTwoStepAuthForUserWithId(String userId, String token) {
        validateNotNull(userId, "User id is required");
        validateNotNull(token, "Token should be provided");
        onReceive(token, userId);
        return userService.getUserById(userId);
    }

}