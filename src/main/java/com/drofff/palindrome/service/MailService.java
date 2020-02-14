package com.drofff.palindrome.service;

import com.drofff.palindrome.type.Mail;

interface MailService {

	void sendMailTo(Mail mail, String ... receivers);

}
