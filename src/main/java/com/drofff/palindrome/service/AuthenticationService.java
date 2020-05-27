package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;

public interface AuthenticationService {

	void registerDriverAccount(User user);

	void activateUserAccountByToken(String userId, String token);

	void requestPasswordRecovery(String email);

	void completePasswordRecoveryOfUserWithIdUsingToken(String userId, String token, String newPassword);

	void requestPasswordChange(String newPassword);

	void confirmPasswordChangeUsingToken(String token);

	void changePasswordUsingOldPassword(String password, String newPassword);

}
