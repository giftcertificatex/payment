package com.giftok.payment.processor.stripe;

import java.util.Currency;
import java.util.HashMap;

import com.giftok.payment.processor.ChargeRequest;
import com.giftok.payment.processor.ChargeResponse;
import com.giftok.payment.processor.PaymentGateway;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import static com.stripe.net.RequestOptions.*;

public class StripePaymentGateway implements PaymentGateway {

	private String stripeDescription = "Gift Ok Payment";

	private Currency euro = Currency.getInstance("EUR");

	private final RequestOptions requestOptions;

	public StripePaymentGateway(String apiKey) {
		this.requestOptions = new RequestOptionsBuilder().setApiKey(apiKey).build();
	}

	@Override
	public ChargeResponse charge(ChargeRequest chargeRequest) {
		return chargeToStripe(chargeRequest);
	}

	private ChargeResponse chargeToStripe(ChargeRequest chargeRequest) {

		try {
			var charge = Charge.create(chargeParams(chargeRequest), requestOptions);
			return ChargeResponse.success(charge.getId());
		} catch (StripeException e) {
			return ChargeResponse.failed(e.getMessage());
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
