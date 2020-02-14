package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;

public interface AuthenticationService {

	void registerDriverAccount(User user);

	void activateUserAccountByToken(String userId, String token);

	void remindPasswordToUserWithEmail(String email);

	void verifyRecoveryAttemptForUserWithToken(String userId, String token);

	void recoverPasswordForUserWithToken(String userId, String token, String newPassword);

}
