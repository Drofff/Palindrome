package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.MailUtils.getAccountBlockedMailWithReason;
import static com.drofff.palindrome.utils.MailUtils.getAccountUnblockedMail;
import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.UserBlock;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.UserBlockRepository;
import com.drofff.palindrome.type.Mail;

@Service
public class UserBlockServiceImpl implements UserBlockService {

	private final UserBlockRepository userBlockRepository;
	private final MailService mailService;

	@Autowired
	public UserBlockServiceImpl(UserBlockRepository userBlockRepository, MailService mailService) {
		this.userBlockRepository = userBlockRepository;
		this.mailService = mailService;
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
		sendUserBlockReasonByMail(user, reason);
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

	private void sendUserBlockReasonByMail(User user, String reason) {
		Mail mail = getAccountBlockedMailWithReason(reason);
		mailService.sendMailTo(mail, user.getUsername());
	}

	@Override
	public void unblockUser(User user) {
		User currentUser = getCurrentUser();
		validateIsAdmin(currentUser);
		validateIsBlocked(user);
		getUserBlockForUserIfPresent(user)
				.ifPresent(userBlockRepository::delete);
		sendUserUnblockedNotificationByMail(user);
	}

	private void validateIsAdmin(User user) {
		if(user.isNotAdmin()) {
			throw new ValidationException("User should obtain admin role");
		}
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

	private void sendUserUnblockedNotificationByMail(User user) {
		Mail accountUnblockedMail = getAccountUnblockedMail();
		mailService.sendMailTo(accountUnblockedMail, user.getUsername());
	}

	@Override
	public long countBlockedUsers() {
		return userBlockRepository.count();
	}

}
