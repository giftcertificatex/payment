package com.giftok.payment.firestore;

import java.util.function.Function;

import com.giftok.payment.processor.ChargeResponse;

public interface PaymentRepository {

	Function<ChargeResponse, Boolean> saveChargeResponse(String certificateId);
	
}
