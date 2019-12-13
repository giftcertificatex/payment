package com.giftok.payment.stripe;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.giftok.payment.ChargeRequest;

/**
 * See https://stripe.com/docs/testing for tokens and scenarios
 * 
 * @author dmytro.tyshchenko
 *
 */

public class StripePaymentProcessorTest {

	@Test
	public void shouldCharge() throws Exception {

		var chargeRequest = new ChargeRequest("tok_fr", 100);
		var stripePaymentProcessor = new StripePaymentProcessor();
		var chargeResponse = stripePaymentProcessor.charge(chargeRequest);
		var result = chargeResponse.errorCode().orElseGet(() -> "success");
		assertEquals("success", result);
	}

	@Test
	public void shouldFailWithInvalidToken() throws Exception {

		var chargeRequest = new ChargeRequest("invalid_token", 100);
		var stripePaymentProcessor = new StripePaymentProcessor();
		var chargeResponse = stripePaymentProcessor.charge(chargeRequest);
		var result = chargeResponse.errorCode().orElseGet(() -> "success");
		assertTrue(result.indexOf("No such token") != -1);
	}
}
