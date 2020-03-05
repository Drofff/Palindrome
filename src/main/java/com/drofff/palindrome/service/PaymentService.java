package com.drofff.palindrome.service;

import com.drofff.palindrome.type.Payment;

public interface PaymentService {

	void executePayment(Payment payment);

	String getPublicKey();

}
