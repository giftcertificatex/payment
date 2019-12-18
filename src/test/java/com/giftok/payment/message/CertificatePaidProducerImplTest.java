package com.giftok.payment.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static com.giftok.payment.message.CertificatePaidProducerImpl.Operations.*;

import org.junit.jupiter.api.Test;

import com.giftok.payment.charge.ChargeResponse;

public class CertificatePaidProducerImplTest {

	@Test
	public void shouldCreatePaymentMessage() throws Exception {
		var paymentMessage = createPaymnetMessage("certId").apply(new ChargeResponse());
		assertEquals("certId", paymentMessage.getCerteficateId());
	}

	@Test
	public void shouldCreateErrorPaymentMessage() throws Exception {
		var response = new ChargeResponse();
		response.setError("Error");
		var paymentMessage = createPaymnetMessage("certId").apply(response);
		assertEquals("Error", paymentMessage.getError());
	}

	@Test
	public void shouldCreatePubsubMessage() throws Exception {
		var paymentMessage = createPaymnetMessage("certId").apply(new ChargeResponse());
		var result = toByteString.andThen(toPubsubMessage).apply(paymentMessage);
		assertNotNull(result);
	}
}
