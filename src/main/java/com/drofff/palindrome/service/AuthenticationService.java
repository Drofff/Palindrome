package com.drofff.palindrome.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.enums.Role;

public interface AuthenticationService {

	void registerDriverAccount(User user);

	void activateUserAccountByToken(String userId, String token);

	void remindPasswordToUserWithEmail(String email);

	void verifyRecoveryAttemptForUserWithToken(String userId, String token);

	void recoverPasswordForUserWithToken(String userId, String token, String newPassword);

	void changeUserPassword(String password, String newPassword);

	void changeUserPasswordByMail(String newPassword);

	void confirmUserPasswordChangeByToken(String token);

	Page<User> getAllUsersAtPage(int page);

	long countUsers();

	List<Role> getAllRoles();

	User getUserById(String id);

	void createUser(User user);

}
