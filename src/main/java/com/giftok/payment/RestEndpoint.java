package com.giftok.payment;

import static spark.Spark.post;

import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.pubsub.v1.PubsubMessage;

import spark.Route;
import spark.Spark;

public class RestEndpoint {

	public static void startListen() {
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
		return "Ok";
	};

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
