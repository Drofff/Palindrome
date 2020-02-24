package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.UserBlock;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.UserBlockRepository;

@Service
public class UserBlockServiceImpl implements UserBlockService {

	private final UserBlockRepository userBlockRepository;

	@Autowired
	public UserBlockServiceImpl(UserBlockRepository userBlockRepository) {
		this.userBlockRepository = userBlockRepository;
	}

	@Override
	public void blockUserByReason(User user, String reason) {
		validateReason(reason);
		User currentUser = getCurrentUser();
		validateIsAdmin(currentUser);
		validateIsNotBlocked(user);
		UserBlock userBlock = createUserBlockWithReason(user, reason);
		validate(userBlock);
		userBlockRepository.save(userBlock);
	}

	private void validateReason(String reason) {
		validateNotNull(reason, "Reason is required");
		validateReasonNotEmpty(reason);
	}

	private void validateReasonNotEmpty(String reason) {
		if(reason.isEmpty()) {
			throw new ValidationException("Reason should not be empty");
		}
	}

	private void validateIsNotBlocked(User user) {
		if(isUserBlocked(user)) {
			throw new ValidationException("User is already blocked");
		}
	}

	private UserBlock createUserBlockWithReason(User user, String reason) {
		return new UserBlock.Builder()
				.userId(user.getId())
				.reason(reason)
				.now()
				.build();
	}

	@Override
	public void unblockUser(User user) {
		User currentUser = getCurrentUser();
		validateIsAdmin(currentUser);
		validateIsBlocked(user);
		getUserBlockForUserIfPresent(user)
				.ifPresent(userBlockRepository::delete);
	}

	private void validateIsAdmin(User user) {
		if(isNotAdmin(user)) {
			throw new ValidationException("User should obtain admin role");
		}
	}

	private boolean isNotAdmin(User user) {
		return !user.isAdmin();
	}

	private void validateIsBlocked(User user) {
		if(isNotUserBlocked(user)) {
			throw new ValidationException("User is not blocked");
		}
	}

	private boolean isNotUserBlocked(User user) {
		return !isUserBlocked(user);
	}

	@Override
	public boolean isUserBlocked(User user) {
		return getUserBlockForUserIfPresent(user).isPresent();
	}

	@Override
	public Optional<UserBlock> getUserBlockForUserIfPresent(User user) {
		return userBlockRepository.findByUserId(user.getId());
	}

}
