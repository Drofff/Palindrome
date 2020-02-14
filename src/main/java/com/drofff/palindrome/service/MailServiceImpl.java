package com.drofff.palindrome.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.exception.MailException;
import com.drofff.palindrome.type.Mail;

@Service
class MailServiceImpl implements MailService {

	private static final String MAIL_ENCODING = StandardCharsets.UTF_8.name();

	private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("^.+@.+\\..+$");

	private static final Boolean IS_HTML_MAIL = Boolean.TRUE;

	private final JavaMailSender javaMailSender;

	@Value("${mail.sender.address}")
	private String senderAddress;

	@Autowired
	public MailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendMailTo(Mail mail, String ... receivers) {
		try {
			validateReceivers(receivers);
			sendMail(mail, receivers);
		} catch(MessagingException e) {
			throw new MailException(e.getMessage());
		}
	}

	private void validateReceivers(String ... receivers) {
		validateHasReceivers(receivers);
		Arrays.stream(receivers)
				.forEach(this::validateReceiver);
	}

	private void validateHasReceivers(String ... receivers) {
		if(hasNoReceivers(receivers)) {
			throw new MailException("At least one receiver should be specified");
		}
	}

	private boolean hasNoReceivers(String ... receivers) {
		return receivers.length == 0;
	}

	private void validateReceiver(String receiver) {
		if(hasInvalidEmailFormat(receiver)) {
			throw new MailException("Email address [" + receiver + "] is of invalid format");
		}
	}

	private boolean hasInvalidEmailFormat(String str) {
		return !hasEmailFormat(str);
	}

	private boolean hasEmailFormat(String str) {
		return EMAIL_ADDRESS_PATTERN.matcher(str)
				.find();
	}

	private void sendMail(Mail mail, String ... receivers) throws MessagingException {
		MimeMessage mimeMessage = buildMimeMessage(mail, receivers);
		javaMailSender.send(mimeMessage);
	}

	private MimeMessage buildMimeMessage(Mail mail, String ... receivers) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MAIL_ENCODING);
		mimeMessageHelper.setFrom(senderAddress);
		mimeMessageHelper.setTo(receivers);
		mimeMessageHelper.setSubject(mail.getTopic());
		mimeMessageHelper.setText(mail.getText(), IS_HTML_MAIL);
		return mimeMessageHelper.getMimeMessage();
	}

}
