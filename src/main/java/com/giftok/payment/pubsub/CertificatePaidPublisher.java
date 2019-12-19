package com.giftok.payment.pubsub;

import static com.giftok.payment.pubsub.CertificatePaidPublisher.Operations.*;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import com.giftok.payment.LogUtility;
import com.giftok.payment.message.PaymentMessageOuterClass.PaymentMessage;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import java.util.concurrent.ExecutionException;

public class CertificatePaidPublisher {

	private static final String topicId = "certificate-paid-topic";
	private static final String projectId = "single-outrider-260808";

	ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);

	public void publish(PaymentMessage message) {

		var result = toByteString.andThen(toPubsubMessage).andThen(publish).apply(message);

		if (result.isEmpty()) {
			LogUtility.error("Can't publish paid result for certificateId: " + message.getCerteficateId(), this.getClass());
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
			LogUtility.error(e.getMessage(), this.getClass());
			return Optional.empty();
		}
	};

	public static class Operations {

		static Function<PaymentMessage, ByteString> toByteString = paymentMessage -> paymentMessage.toByteString();

		static Function<ByteString, PubsubMessage> toPubsubMessage = byteString -> {
			return PubsubMessage.newBuilder().setData(byteString).build();
		};
	}
}
