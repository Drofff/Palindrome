package com.drofff.palindrome.type;

import com.drofff.palindrome.enums.Currency;

public class PaymentHistory {

	private Long amount;

	private Currency currency;

	private String chargeId;

	private String violationId;

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

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}

	public String getViolationId() {
		return violationId;
	}

	public void setViolationId(String violationId) {
		this.violationId = violationId;
	}

	public static class Builder {

		private PaymentHistory paymentHistory = new PaymentHistory();

		public Builder amount(Long amount) {
			paymentHistory.amount = amount;
			return this;
		}

		public Builder currency(Currency currency) {
			paymentHistory.currency = currency;
			return this;
		}

		public Builder chargeId(String chargeId) {
			paymentHistory.chargeId = chargeId;
			return this;
		}

		public PaymentHistory build() {
			return paymentHistory;
		}

	}

}
