package com.giftok.payment;

import static spark.Spark.post;

import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

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

	private static Gson gson = new Gson();

	static Function<String, String> getMessage = str -> {
		return getMessage(str);
	};

	static Function<String, String> getData = str -> {
		return getData(str);
	};

	static Route postPaymentRoute = (req, res) -> {
		System.out.println(getMessage.andThen(getData).apply(req.body()));
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
}
