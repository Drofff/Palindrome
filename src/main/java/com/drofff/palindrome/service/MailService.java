package com.drofff.palindrome.service;

import com.drofff.palindrome.type.Mail;

public interface MailService {

	void sendMailTo(Mail mail, String ... receivers);

}
