package com.drofff.palindrome.document;

import com.drofff.palindrome.enums.Currency;

public class Fee {

	private Long amount;

	private Currency currency;

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

}
