package com.giftok.payment;

import static spark.Spark.post;

import java.util.Optional;
import java.util.function.Function;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.giftok.payment.charge.ChargeResponse;
import com.giftok.payment.message.CertificateCreatedConsumerImpl;
import com.giftok.payment.message.CertificatePaidProducerImpl;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.pubsub.v1.PubsubMessage;

import spark.Route;
import spark.Spark;

public class RestEndpoint {

	public static void startListening() {
		Spark.port(8080);
		int cores = Runtime.getRuntime().availableProcessors();
		Spark.threadPool(cores * 2, cores, 5 * 1000);
		post("/submitPayment", postPaymentRoute);
	}

	/**
	 * Expects message from Google Pub/Sub
	 */

	static Function<String, String> getMessage = str -> getMessage(str);

	static Function<String, String> getData = str -> getData(str);

	static Function<String, PubsubMessage> toPubSubMessage = str -> toPubSubMessage(str);

	static Route postPaymentRoute = (req, res) -> {
		var pubSubMessage = getMessage.andThen(toPubSubMessage).apply(req.body());

		var createCert = createCertificateMessage(pubSubMessage);
		// TODO refactor this
		if (createCert.isPresent()) {
			var certificate = createCert.get();
			var chargeResponse = charge(certificate);
			publishResponse(certificate.getId(), chargeResponse);
			res.status(200);
			return "Ok";
		} else {
			res.status(500);
			return "Failed";
		}
	};

	private static ChargeResponse charge(CertificateMessage message) {
		var consumer = new CertificateCreatedConsumerImpl();
		var chargeResponse = consumer.processMessage(message);
		return chargeResponse;
	}

	private static void publishResponse(String certificateId, ChargeResponse chargeResponse) {

		var producer = new CertificatePaidProducerImpl();
		producer.publish(certificateId, chargeResponse);

	}

	private static Optional<CertificateMessage> createCertificateMessage(PubsubMessage pubSubMessage) {
		try {
			return Optional.of(CertificateMessage.parseFrom(pubSubMessage.getData()));
		} catch (InvalidProtocolBufferException e) {
			LogUtility.error(e.getMessage());
			return Optional.empty();
		}
	}

	private static String getData(String message) {
		var jsonRoot = JsonParser.parseString(message);
		var dataStr = jsonRoot.getAsJsonObject().get("data").toString();
		return dataStr;
	}

	private static String getMessage(String body) {
		var jsonRoot = JsonParser.parseString(body);
		var messageStr = jsonRoot.getAsJsonObject().get("message").toString();
		return messageStr;
	}

	private static PubsubMessage toPubSubMessage(String message) {
		Gson gson = new Gson();
		PubsubMessage pubSubMessage = gson.fromJson(message, PubsubMessage.class);
		return pubSubMessage;
	}
}
