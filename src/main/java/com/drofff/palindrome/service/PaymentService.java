package com.drofff.palindrome.service;

import com.drofff.palindrome.type.Payment;
import com.drofff.palindrome.type.PaymentHistory;

public interface PaymentService {

	PaymentHistory executePayment(Payment payment);

	String getPublicKey();

}
