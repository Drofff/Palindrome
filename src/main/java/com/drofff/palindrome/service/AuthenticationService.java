package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;

public interface AuthenticationService {

	void registerDriverAccount(User user);

	void activateUserAccountByToken(String userId, String token);

	void remindPasswordToUserWithEmail(String email);

	void verifyRecoveryAttemptForUserByToken(String userId, String token);

	void changeUserPasswordByToken(String userId, String token, String newPassword);

	void changeUserPassword(String password, String newPassword);

	void changeUserPasswordByMail(String newPassword);

	void confirmUserPasswordChangeByToken(String token);

	User authenticateUserByCredentials(String username, String password);

}
