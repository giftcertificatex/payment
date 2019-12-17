package com.giftok.payment.charge;

import com.giftok.payment.charge.stripe.StripePaymentProcessor;

public interface PaymentProcessor {

	ChargeResponse charge(ChargeRequest chargeRequest);
	
	public static PaymentProcessor getInstance() {
		return new StripePaymentProcessor();
	}
}
