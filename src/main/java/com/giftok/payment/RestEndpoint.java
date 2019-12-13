package com.giftok.payment;

import static spark.Spark.post;

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
	
	static Route postPaymentRoute = (req, res) -> {
		System.out.println(getMessageString(req.body()));
		return "Ok";
	};
	
	private static String getMessageString(String body) {
		var jsonRoot = JsonParser.parseString(body);
	    var messageStr = jsonRoot.getAsJsonObject().get("message").toString();
	    return messageStr;
	}
}
