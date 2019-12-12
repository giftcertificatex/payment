package com.giftok.payment;

public interface PaymentProcessor {

	ChargeResponse charge(ChargeRequest chargeRequest);
}
