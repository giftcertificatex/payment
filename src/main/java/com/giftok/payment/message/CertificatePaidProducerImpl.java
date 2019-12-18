package com.giftok.payment.message;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import com.giftok.payment.LogUtility;
import com.giftok.payment.charge.ChargeResponse;
import com.giftok.paymnet.message.PaymentMessageOuterClass.PaymentMessage;
import com.giftok.paymnet.message.PaymentMessageOuterClass.PaymentMessage.Builder;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import static com.giftok.payment.message.CertificatePaidProducerImpl.Operations.*;

import java.util.concurrent.ExecutionException;

public class CertificatePaidProducerImpl implements CertificatePaidProducer {

	private static final String topicId = "certificate-paid-topic";
	private static final String projectId = "single-outrider-260808";

	ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);

	@Override
	public void publish(String certificateId, ChargeResponse chargeResponse) {

		Optional<String> result = createPaymnetMessage(certificateId).andThen(toByteString.andThen(toPubsubMessage).andThen(publish))
				.apply(chargeResponse);
		
		if (result.isEmpty()) {
			LogUtility.error("Can't publish paid result for certificateId: "+certificateId);
		}
	}

	// TODO maybe make a published field reference
	Function<PubsubMessage, Optional<String>> publish = pubSubMessage -> {
		try {
			var publisher = Publisher.newBuilder(topicName).build();
			var futureRes = publisher.publish(pubSubMessage);
			String messageID = futureRes.get();
			publisher.shutdown();
			return Optional.of(messageID);
		} catch (IOException | InterruptedException | ExecutionException e) {
			LogUtility.error(e.getMessage());
			return Optional.empty();
		}
	};

	public static class Operations {

		static Function<ChargeResponse, PaymentMessage> createPaymnetMessage(String certificateId) {
			return chargeResponse -> {
				Builder builder = PaymentMessage.newBuilder().setCerteficateId(certificateId);
				chargeResponse.error().ifPresent(value -> builder.setError(value));
				return builder.build();
			};
		}

		static Function<PaymentMessage, ByteString> toByteString = paymentMessage -> paymentMessage.toByteString();

		static Function<ByteString, PubsubMessage> toPubsubMessage = byteString -> {
			return PubsubMessage.newBuilder().setData(byteString).build();
		};
	}
}
