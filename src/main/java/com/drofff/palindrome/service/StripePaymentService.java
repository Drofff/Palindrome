package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.configuration.properties.StripeProperties;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.type.Payment;
import com.drofff.palindrome.type.PaymentHistory;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;

@Service
public class StripePaymentService implements PaymentService {

	private static final String SUCCEEDED_STATUS = "succeeded";

	private final StripeProperties stripeProperties;

	@Autowired
	public StripePaymentService(StripeProperties stripeProperties) {
		this.stripeProperties = stripeProperties;
		String secretKey = stripeProperties.getSecretKey();
		validateNotNull(secretKey, "Secret api key is required");
		Stripe.apiKey = secretKey;
	}

	@Override
	public PaymentHistory executePayment(Payment payment) {
		validateNotNull(payment, "Missing payment data");
		validate(payment);
		ChargeCreateParams chargeCreateParams = chargeParamsFromPayment(payment);
		Charge charge = createChargeWithParams(chargeCreateParams);
		validateIsSuccessful(charge);
		return historyOfPaymentWithChargeId(payment, charge.getId());
	}

	private ChargeCreateParams chargeParamsFromPayment(Payment payment) {
		return ChargeCreateParams.builder()
				.setAmount(payment.getAmount())
				.setCurrency(payment.getCurrency().name())
				.setDescription(payment.getDescription())
				.setSource(payment.getPaymentToken())
				.build();
	}

	private Charge createChargeWithParams(ChargeCreateParams params) {
		try {
			return Charge.create(params);
		} catch(StripeException e) {
			throw new PalindromeException(e.getMessage());
		}
	}

	private void validateIsSuccessful(Charge charge) {
		if(hasStatusNotSucceeded(charge)) {
			throw new PalindromeException("Payment status is " + charge.getStatus());
		}
	}

	private boolean hasStatusNotSucceeded(Charge charge) {
		return !hasStatusSucceeded(charge);
	}

	private boolean hasStatusSucceeded(Charge charge) {
		return charge.getStatus().equals(SUCCEEDED_STATUS);
	}

	private PaymentHistory historyOfPaymentWithChargeId(Payment payment, String chargeId) {
		return new PaymentHistory.Builder()
				.amount(payment.getAmount())
				.currency(payment.getCurrency())
				.chargeId(chargeId)
				.build();
	}

	@Override
	public String getPublicKey() {
		return stripeProperties.getPublicKey();
	}

}
