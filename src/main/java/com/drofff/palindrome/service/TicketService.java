package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.type.PaymentHistory;
import com.drofff.palindrome.type.TicketFile;

public interface TicketService {

	void saveTicketForPayment(PaymentHistory paymentHistory);

	TicketFile getPayedViolationTicket(Violation violation);

}
