package com.giftok.payment.stripe;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.giftok.payment.processor.ChargeRequest;
import com.giftok.payment.processor.stripe.StripePaymentGateway;

/**
 * See https://stripe.com/docs/testing for tokens and scenarios
 * 
 * @author dmytro.tyshchenko
 *
 */

public class StripePaymentProcessorTest {

	private final String stripeApiKey = "sk_test_6kK5at10gSmpC7v9XJHhkxgB00r6oYzklj";
	
	@Test
	public void shouldCharge() throws Exception {

		var chargeRequest = new ChargeRequest("tok_fr", 100);
		var stripePaymentProcessor = new StripePaymentGateway(stripeApiKey);
		var chargeResponse = stripePaymentProcessor.charge(chargeRequest);
		var result = chargeResponse.error().orElseGet(() -> "success");
		assertEquals("success", result);
	}

	@Test
	public void shouldFailWithInvalidToken() throws Exception {

		var chargeRequest = new ChargeRequest("invalid_token", 100);
		var stripePaymentProcessor = new StripePaymentGateway(stripeApiKey);
		var chargeResponse = stripePaymentProcessor.charge(chargeRequest);
		var result = chargeResponse.error().orElseGet(() -> "success");
		assertTrue(result.indexOf("No such token") != -1);
	}
}
