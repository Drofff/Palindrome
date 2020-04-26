package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.TwoStepAuthException;
import com.drofff.palindrome.type.ExternalAuthenticationOption;

import java.util.HashMap;
import java.util.Map;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.StringUtils.areNotEqual;
import static com.drofff.palindrome.utils.StringUtils.randomString;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static java.lang.System.currentTimeMillis;

public abstract class TokenAuthenticator implements ExternalAuthenticator {

    private final Map<String, String> tokensBuffer = new HashMap<>();

    @Override
    public void authenticateUsingOptionWithId(String optionId) {
        validateHasOptionWithId(optionId);
        User currentUser = getCurrentUser();
        removeUserTokenFromBuffer(currentUser);
        String token = randomString();
        sendTokenToAuthenticatorOfOptionWithId(token, optionId);
        waitTillUserAuthComplete(currentUser);
        validateUserAuthCompleted(currentUser);
        String receivedToken = getTokenReceivedForUser(currentUser);
        validateToken(receivedToken, token);
    }

    private void removeUserTokenFromBuffer(User user) {
        synchronized(tokensBuffer) {
            tokensBuffer.remove(user.getId());
        }
    }

    protected abstract void sendTokenToAuthenticatorOfOptionWithId(String token, String optionId);

    private void waitTillUserAuthComplete(User user) {
        try {
            waitForUserToken(user);
        } catch(InterruptedException e) {
            throw new PalindromeException("Token wait thread has been interrupted");
        }
    }

    private void waitForUserToken(User user) throws InterruptedException {
        long waitStartTime = currentTimeMillis();
        synchronized(tokensBuffer) {
            while(shouldWaitForUserToken(user, waitStartTime)) {
                tokensBuffer.wait(getMaxTokenWaitTime());
            }
        }
    }

    private boolean shouldWaitForUserToken(User user, long waitStartTime) {
        return hasNoTokenForUserId(user.getId()) && waitTimeIsNotOut(waitStartTime);
    }

    private boolean waitTimeIsNotOut(long waitStartTime) {
        return calculateTimeWaiting(waitStartTime) <= getMaxTokenWaitTime();
    }

    protected abstract long getMaxTokenWaitTime();

    private long calculateTimeWaiting(long waitStartTime) {
        return currentTimeMillis() - waitStartTime;
    }

    private void validateUserAuthCompleted(User user) {
        if(hasNoTokenForUserId(user.getId())) {
            throw new TwoStepAuthException("Authentication confirmation has not been received");
        }
    }

    private boolean hasNoTokenForUserId(String userId) {
        synchronized(tokensBuffer) {
            return !tokensBuffer.containsKey(userId);
        }
    }

    private String getTokenReceivedForUser(User user) {
        synchronized(tokensBuffer) {
            return tokensBuffer.get(user.getId());
        }
    }

    private void validateToken(String receivedToken, String originalToken) {
        if(areNotEqual(originalToken, receivedToken)) {
            throw new TwoStepAuthException("Token is invalid");
        }
    }

    @Override
    public void completeAuthenticationWithOptionId(String optionId, String token) {
        validateNotNull(token, "Token is required");
        validateHasOptionWithId(optionId);
        User currentUser = getCurrentUser();
        populateBufferWithTokenOfUser(token, currentUser);
    }

    private void validateHasOptionWithId(String optionId) {
        if(hasNoOptionWithId(optionId)) {
            throw new TwoStepAuthException("Unknown option id");
        }
    }

    private boolean hasNoOptionWithId(String optionId) {
        return !hasOptionWithId(optionId);
    }

    private void populateBufferWithTokenOfUser(String token, User user) {
        synchronized(tokensBuffer) {
            tokensBuffer.put(user.getId(), token);
            tokensBuffer.notifyAll();
        }
    }

    @Override
    public boolean hasOptionWithId(String optionId) {
        return getAuthenticationOptions().stream()
                .anyMatch(option -> hasOptionId(option, optionId));
    }

    private boolean hasOptionId(ExternalAuthenticationOption option, String optionId) {
        return option.getId().equals(optionId);
    }

}
