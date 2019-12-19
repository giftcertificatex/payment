package com.giftok.payment;

import java.util.logging.Logger;


public class LogUtility {
	
	//private static final Logging logging = LoggingOptions.getDefaultInstance().getService();
	 private static final Logger logger = Logger.getLogger("Payment Service");
	
	public static void error(String message) {
		logger.severe(message);
	}
	
	public static void debug(String message) {
		logger.warning(message);
	}
}
