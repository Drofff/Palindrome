package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.FormattingUtils.putParamsIntoText;
import static com.drofff.palindrome.utils.JsonUtils.getJSONObjectFromFileByKey;

import java.util.Arrays;
import java.util.Deque;

import org.json.simple.JSONObject;
import org.springframework.data.util.Pair;

import com.drofff.palindrome.collector.PairDequeCollector;
import com.drofff.palindrome.type.Mail;

public class MailUtils {

	private static final String MAIL_MESSAGES_FILE = "mail_messages.json";

	private static final String LINK_PARAM = "${link}";
	private static final String USERNAME_PARAM = "${username}";
	private static final String REASON_PARAM = "${reason}";
	private static final String PASSWRD_PARAM = "${password}";
	private static final String NUMBER_PARAM = "${number}";
	private static final String FIRST_NAME_PARAM = "${firstName}";

	private static final String ACTIVATION_MAIL_KEY = "activation";
	private static final String REMIND_PASS_MAIL_KEY = "remind-password";
	private static final String ACCOUNT_BLOCKED_MAIL_KEY = "account-blocked";
	private static final String ACCOUNT_UNBLOCKED_MAIL_KEY = "account-unblocked";
	private static final String CREDENTIALS_MAIL_KEY = "credentials-created";
	private static final String CAR_UPDATE_MAIL_KEY = "car-update";
	private static final String CHANGE_REQUEST_APPROVED_SENDER_MAIL_KEY = "request-approved-sender";
	private static final String CHANGE_REQUEST_APPROVED_DRIVER_MAIL_KEY = "request-approved-driver";
	private static final String CHANGE_REQUEST_REFUSED_MAIL_KEY = "request-refused";
	private static final String CHANGE_PASS_CONFIRMATION_MAIL_KEY = "change-password";
	private static final String VIOLATION_ADDED_MAIL_KEY = "violation-added";
	private static final String TWO_STEP_AUTH_MAIL_KEY = "two-step-auth";

	private MailUtils() {}

	public static Mail getTwoStepAuthMail(String firstName, String link) {
		return mailByKeyWithParams(TWO_STEP_AUTH_MAIL_KEY, FIRST_NAME_PARAM, firstName, LINK_PARAM, link);
	}

	public static Mail getViolationAddedMail(String link) {
		return mailByKeyWithParams(VIOLATION_ADDED_MAIL_KEY, LINK_PARAM, link);
	}

	public static Mail getPasswordChangeConfirmationMail(String link) {
		return mailByKeyWithParams(CHANGE_PASS_CONFIRMATION_MAIL_KEY, LINK_PARAM, link);
	}

	public static Mail getChangeRequestApprovedSenderMail(String senderFirstName, String driverUsername) {
		return mailByKeyWithParams(CHANGE_REQUEST_APPROVED_SENDER_MAIL_KEY, FIRST_NAME_PARAM, senderFirstName, USERNAME_PARAM, driverUsername);
	}

	public static Mail getChangeRequestApprovedDriverMail(String driverFirstName) {
		return mailByKeyWithParams(CHANGE_REQUEST_APPROVED_DRIVER_MAIL_KEY, FIRST_NAME_PARAM, driverFirstName);
	}

	public static Mail getChangeRequestRefusedMail(String senderFirstName, String driverUsername) {
		return mailByKeyWithParams(CHANGE_REQUEST_REFUSED_MAIL_KEY, FIRST_NAME_PARAM, senderFirstName, USERNAME_PARAM, driverUsername);
	}

	public static Mail getCarUpdateMail(String carNumber) {
		 return mailByKeyWithParams(CAR_UPDATE_MAIL_KEY, NUMBER_PARAM, carNumber);
	}

	public static Mail getCredentialsMail(String username, String password) {
		return mailByKeyWithParams(CREDENTIALS_MAIL_KEY, USERNAME_PARAM, username, PASSWRD_PARAM, password);
	}

	public static Mail getAccountBlockedMail(String reason) {
		return mailByKeyWithParams(ACCOUNT_BLOCKED_MAIL_KEY, REASON_PARAM, reason);
	}

	public static Mail getActivationMail(String link, String username) {
		return mailByKeyWithParams(ACTIVATION_MAIL_KEY, LINK_PARAM, link, USERNAME_PARAM, username);
	}

	public static Mail getRemindPasswordMail(String link) {
		return mailByKeyWithParams(REMIND_PASS_MAIL_KEY, LINK_PARAM, link);
	}

	private static Mail mailByKeyWithParams(String key, String ... params) {
		Mail template = getMailTemplateFromFileByKey(key);
		String templateText = template.getText();
		String mailText = putParamsIntoText(templateText, dequeOfParams(params));
		template.setText(mailText);
		return template;
	}

	private static Deque<Pair<String, String>> dequeOfParams(String ... params) {
		return Arrays.stream(params)
				.collect(new PairDequeCollector<>());
	}

	public static Mail getAccountUnblockedMail() {
		return getMailTemplateFromFileByKey(ACCOUNT_UNBLOCKED_MAIL_KEY);
	}

	private static Mail getMailTemplateFromFileByKey(String key) {
		JSONObject jsonObject = getJSONObjectFromFileByKey(MAIL_MESSAGES_FILE, key);
		return Mail.fromJSONObject(jsonObject);
	}

}
