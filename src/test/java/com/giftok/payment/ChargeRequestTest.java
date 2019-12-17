package com.giftok.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.giftok.payment.charge.ChargeRequest;

public class ChargeRequestTest {

	@Test
	public void shouldCreateChargeRequest() throws Exception {
		var chargeRequest = new ChargeRequest("testToken", 333);
		assertEquals("testToken", chargeRequest.cardToken());
		assertEquals(333, chargeRequest.amount());
	}

}
