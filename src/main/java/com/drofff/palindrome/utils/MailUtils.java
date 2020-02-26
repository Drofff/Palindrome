package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.FormattingUtils.putParamsIntoText;

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

	private static final String ACTIVATION_MAIL_KEY = "activation";
	private static final String REMIND_PASS_MAIL_KEY = "remind-password";
	private static final String ACCOUNT_BLOCKED_MAIL_KEY = "account-blocked";
	private static final String ACCOUNT_UNBLOCKED_MAIL_KEY = "account-unblocked";
	private static final String CREDENTIALS_MAIL_KEY = "credentials-created";

	private MailUtils() {}

	public static Mail getCredentialsMail(String username, String password) {
		return mailByKeyWithParams(CREDENTIALS_MAIL_KEY, USERNAME_PARAM, username, PASSWRD_PARAM, password);
	}

	public static Mail getAccountBlockedMailWithReason(String reason) {
		return mailByKeyWithParams(ACCOUNT_BLOCKED_MAIL_KEY, REASON_PARAM, reason);
	}

	public static Mail getActivationMailWithLinkAndUsername(String link, String username) {
		return mailByKeyWithParams(ACTIVATION_MAIL_KEY, LINK_PARAM, link, USERNAME_PARAM, username);
	}

	public static Mail getRemindPasswordMailWithLink(String link) {
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
		JSONObject jsonObject = JsonUtils.getObjectFromFileByKey(MAIL_MESSAGES_FILE, key);
		return Mail.fromJSONObject(jsonObject);
	}

}
