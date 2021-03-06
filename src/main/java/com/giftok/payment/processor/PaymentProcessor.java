package com.giftok.payment.processor;

import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.giftok.payment.LogUtility;
import com.giftok.payment.firestore.PaymentRepository;
import com.giftok.payment.message.PaymentMessageOuterClass.PaymentMessage;
import com.giftok.payment.message.PaymentMessageOuterClass.PaymentMessage.Builder;

import static com.giftok.payment.processor.PaymentProcessor.Operations.*;

public class PaymentProcessor {

	private final PaymentGateway paymentGateway;
	private final BlockingQueue<PaymentMessage> paymentMessageQueue;
	private final PaymentRepository paymentRepository;

	public PaymentProcessor(PaymentGateway paymentGateway, BlockingQueue<PaymentMessage> paymentMessageQueue,
			PaymentRepository paymentRepository) {
		this.paymentGateway = paymentGateway;
		this.paymentMessageQueue = paymentMessageQueue;
		this.paymentRepository = paymentRepository;
	}

	public void process(CertificateMessage certificateMessage) {
		var certificateId = certificateMessage.getId();
		var paymentMessage = createChargeRequest
				.andThen(charge(this.paymentGateway).andThen(saveChargeResponse(certificateId)))
				.andThen(createPaymnetMessage(certificateId)).apply(certificateMessage);
		putToQueue(paymentMessage);
	}

	private void putToQueue(PaymentMessage message) {
		try {
			paymentMessageQueue.put(message);
		} catch (InterruptedException e) {
			LogUtility.error("Can't put response to Queue for Certificate: " + message.getCerteficateId(),
					this.getClass());
		}
	}

	private Function<ChargeResponse, ChargeResponse> saveChargeResponse(String certificateId) {
		return chargeResponse -> {
			this.paymentRepository.saveChargeResponse(certificateId).apply(chargeResponse);
			return chargeResponse;
		};
	}

	public static class Operations {

		public static Function<CertificateMessage, ChargeRequest> createChargeRequest = certMessage -> createChargeRequest(
				certMessage);

		public static Function<ChargeRequest, ChargeResponse> charge(PaymentGateway paymentProcessor) {
			return chargeRequest -> paymentProcessor.charge(chargeRequest);
		}

		static private ChargeRequest createChargeRequest(CertificateMessage certificateMessage) {

			var result = new ChargeRequest(certificateMessage.getCardHash(), certificateMessage.getAmount());
			return result;
		}

		public static Function<ChargeResponse, PaymentMessage> createPaymnetMessage(String certificateId) {
			return chargeResponse -> {
				Builder builder = PaymentMessage.newBuilder().setCerteficateId(certificateId);
				chargeResponse.error().ifPresent(value -> builder.setError(value));
				return builder.build();
			};
		}
	}
}
