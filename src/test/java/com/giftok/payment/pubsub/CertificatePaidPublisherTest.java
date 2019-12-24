package com.giftok.payment.pubsub;

import static com.giftok.payment.pubsub.CertificatePaidPublisher.Operations.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.giftok.payment.processor.ChargeResponse;
import static com.giftok.payment.processor.PaymentProcessor.Operations.*;

public class CertificatePaidPublisherTest {

	@Test
	public void shouldCreatePaymentMessage() throws Exception {
		var paymentMessage = createPaymnetMessage("certId").apply(ChargeResponse.success("id"));
		assertEquals("certId", paymentMessage.getCerteficateId());
	}

	@Test
	public void shouldCreateErrorPaymentMessage() throws Exception {
		var paymentMessage = createPaymnetMessage("certId").apply(ChargeResponse.failed("Error"));
		assertEquals("Error", paymentMessage.getError());
	}

	@Test
	public void shouldCreatePubsubMessage() throws Exception {
		var paymentMessage = createPaymnetMessage("certId").apply(ChargeResponse.success("id"));
		var result = toByteString.andThen(toPubsubMessage).apply(paymentMessage);
		assertNotNull(result);
	}
}
