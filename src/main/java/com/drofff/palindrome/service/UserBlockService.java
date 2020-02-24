package com.drofff.palindrome.service;

import java.util.Optional;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.UserBlock;

public interface UserBlockService {

	void blockUserByReason(User user, String reason);

	void unblockUser(User user);

	boolean isUserBlocked(User user);

	Optional<UserBlock> getUserBlockForUserIfPresent(User user);

}
