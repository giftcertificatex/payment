package com.giftok.payment.charge.stripe;

import java.util.Currency;
import java.util.HashMap;

import com.giftok.payment.processor.ChargeRequest;
import com.giftok.payment.processor.ChargeResponse;
import com.giftok.payment.processor.PaymentGateway;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public class StripePaymentGateway implements PaymentGateway {

	static {
		// test Api Key of Gift Ok
		Stripe.apiKey = "sk_test_6kK5at10gSmpC7v9XJHhkxgB00r6oYzklj";
	}

	private String stripeDescription = "Gift Ok Payment";

	private Currency euro = Currency.getInstance("EUR");

	@Override
	public ChargeResponse charge(ChargeRequest chargeRequest) {
		return chargeToStripe(chargeRequest);
	}

	private ChargeResponse chargeToStripe(ChargeRequest chargeRequest) {

		try {
			Charge.create(chargeParams(chargeRequest));
			return new ChargeResponse();
		} catch (StripeException e) {
			ChargeResponse response = new ChargeResponse();
			response.setError(e.getMessage());
			return response;
		}
	}

	private HashMap<String, Object> chargeParams(ChargeRequest chargeRequest) {

		HashMap<String, Object> chargeParams = new HashMap<>();
		chargeParams.put("amount", chargeRequest.amount());
		chargeParams.put("currency", euro.getCurrencyCode());
		chargeParams.put("description", stripeDescription);
		chargeParams.put("source", chargeRequest.cardToken());

		return chargeParams;
	}
}
