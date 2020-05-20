package com.drofff.palindrome.document;

import com.drofff.palindrome.enums.Currency;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Fee {

	@NotNull(message = "Amount is required")
	@Min(value = 1, message = "Minimal amount is 1")
	private Long amount;

	@NotNull(message = "Currency should be provided")
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
