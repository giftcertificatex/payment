package com.giftok.payment.charge;

public interface PaymentProcessor {

	ChargeResponse charge(ChargeRequest chargeRequest);
}
