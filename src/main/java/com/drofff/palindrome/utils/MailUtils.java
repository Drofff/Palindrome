package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.FormattingUtils.putParamIntoText;
import static com.drofff.palindrome.utils.FormattingUtils.putParamsIntoText;

import java.util.ArrayDeque;
import java.util.Deque;

import org.json.simple.JSONObject;
import org.springframework.data.util.Pair;

import com.drofff.palindrome.type.Mail;

public class MailUtils {

	private static final String MAIL_MESSAGES_FILE = "mail_messages.json";

	private static final String LINK_PARAM = "${link}";
	private static final String USERNAME_PARAM = "${username}";

	private static final String ACTIVATION_MAIL_KEY = "activation";
	private static final String REMIND_PASS_MAIL_KEY = "remind-password";

	private MailUtils() {}

	public static Mail getActivationMailWithLinkAndUsername(String link, String username) {
		Mail activationMailTemplate = getMailTemplateFromFileByKey(ACTIVATION_MAIL_KEY);
		String textTemplate = activationMailTemplate.getText();
		Deque<Pair<String, String>> activationMessageParams = activationMessageParamsQueue(link, username);
		String textWithParams = putParamsIntoText(textTemplate, activationMessageParams);
		activationMailTemplate.setText(textWithParams);
		return activationMailTemplate;
	}

	private static Deque<Pair<String, String>> activationMessageParamsQueue(String link, String username) {
		Pair<String, String> linkParam = Pair.of(LINK_PARAM, link);
		Pair<String, String> usernameParam = Pair.of(USERNAME_PARAM, username);
		Deque<Pair<String, String>> params = new ArrayDeque<>();
		params.add(linkParam);
		params.add(usernameParam);
		return params;
	}

	public static Mail getRemindPasswordMailWithLink(String link) {
		Mail remindPasswordMailTemplate = getMailTemplateFromFileByKey(REMIND_PASS_MAIL_KEY);
		String templateText = remindPasswordMailTemplate.getText();
		String textWithLink = putParamIntoText(templateText, Pair.of(LINK_PARAM, link));
		remindPasswordMailTemplate.setText(textWithLink);
		return remindPasswordMailTemplate;
	}

	private static Mail getMailTemplateFromFileByKey(String key) {
		JSONObject jsonObject = JsonUtils.getObjectFromFileByKey(MAIL_MESSAGES_FILE, key);
		return Mail.fromJSONObject(jsonObject);
	}

}
