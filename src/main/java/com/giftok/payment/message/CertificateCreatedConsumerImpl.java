package com.giftok.payment.message;

import java.util.Optional;
import java.util.function.Function;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.giftok.payment.LogUtility;
import com.giftok.payment.charge.ChargeRequest;
import com.giftok.payment.charge.ChargeResponse;
import com.giftok.payment.charge.PaymentProcessor;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.pubsub.v1.PubsubMessage;

public class CertificateCreatedConsumerImpl implements CerftificateCreatedConsumer {

	private final PaymentProcessor paymentProcessor;

	public CertificateCreatedConsumerImpl() {

		paymentProcessor = PaymentProcessor.getInstance();
	}

	@Override
	public ChargeResponse processMessage(CertificateMessage message) {
		var result = createChargeRequest.andThen(charge(paymentProcessor)).apply(message);
		return result;
	}

	Function<CertificateMessage, ChargeRequest> createChargeRequest = certMessage -> createChargeRequest(certMessage);

	Function<ChargeRequest, ChargeResponse> charge(PaymentProcessor paymentProcessor) {
		return chargeRequest -> paymentProcessor.charge(chargeRequest);
	}

	private ChargeRequest createChargeRequest(CertificateMessage certificateMessage) {

		var result = new ChargeRequest(certificateMessage.getCardHash(), certificateMessage.getAmount());
		return result;
	}

}
