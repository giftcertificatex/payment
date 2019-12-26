package com.giftok.payment.firestore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.giftok.payment.processor.ChargeResponse;

import static com.giftok.payment.firestore.FirestorePaymentRepository.Operations.*;


public class FirestorePaymentRepositoryTest {

	@Test
	public void shouldCreateSuccessMap() throws Exception {
		
		var chargeResponse = ChargeResponse.success("paymentId");
		var result = createMap(chargeResponse);
		var paymentId = result.get("paymentId");
		assertEquals("paymentId", paymentId);
	}
	
	@Test
	public void shouldCreateErrorMap() throws Exception {
		
		var chargeResponse = ChargeResponse.failed("error");
		var result = createMap(chargeResponse);
		var error = result.get("error");
		assertEquals("error", error);
	}
}
