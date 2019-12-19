package com.giftok.payment.processor;

import com.giftok.payment.processor.stripe.StripePaymentGateway;

public interface PaymentGateway {

	ChargeResponse charge(ChargeRequest chargeRequest);
	
	public static PaymentGateway getInstance() {
		return new StripePaymentGateway();
	}
}
