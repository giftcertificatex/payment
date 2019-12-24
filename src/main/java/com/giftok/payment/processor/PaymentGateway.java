package com.giftok.payment.processor;

public interface PaymentGateway {

	ChargeResponse charge(ChargeRequest chargeRequest);
}
