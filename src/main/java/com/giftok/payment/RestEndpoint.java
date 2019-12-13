package com.giftok.payment;

import static spark.Spark.post;

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

	static Route postPaymentRoute = (req, res) -> {
		System.out.println(req.body());
		return "Ok";
	};
}
