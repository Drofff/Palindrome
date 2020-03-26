package com.drofff.palindrome.type;

import com.drofff.palindrome.document.ViolationType;
import com.drofff.palindrome.enums.Currency;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Payment {

	@NotNull(message = "Amount is required")
	@Min(value = 1, message = "Amount value should be greater than 0")
	private Long amount;

	@NotNull(message = "Currency is required")
	private Currency currency;

	@NotBlank(message = "Payment description is missing")
	private String description;

	@NotBlank(message = "Payment token is required")
	private String paymentToken;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPaymentToken() {
		return paymentToken;
	}

	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}

	public static class Builder {

		private Payment payment = new Payment();

		public Builder forViolationType(ViolationType violationType) {
			payment.amount = violationType.getFee().getAmount();
			payment.currency = violationType.getFee().getCurrency();
			payment.description = violationType.getName();
			return this;
		}

		public Builder withPaymentToken(String token) {
			payment.paymentToken = token;
			return this;
		}

		public Payment build() {
			return payment;
		}

	}

}
