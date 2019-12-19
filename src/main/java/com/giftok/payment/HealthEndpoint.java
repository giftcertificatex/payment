package com.giftok.payment;


import static spark.Spark.get;

import spark.Route;
import spark.Spark;

/**
 * Represent HealthEndpoin which could be used to collect Info about Service
 * state
 * 
 * @author dmytro.tyshchenko
 *
 */

public class HealthEndpoint {

	public static void startListening() {
		int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
		Spark.port(port);
		get("/", healthStatus);

	}

	static Route healthStatus = (req, res) -> {
		LogUtility.info("Got Health Request", HealthEndpoint.class);
		return "Ok";
	};
}
