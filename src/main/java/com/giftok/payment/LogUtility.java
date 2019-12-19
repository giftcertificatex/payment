package com.giftok.payment;

import java.util.logging.Logger;

/**
 * Encapsulates logging operations  
 * 
 * @author dmytro.tyshchenko
 *
 */

public class LogUtility {

	private static final Logger logger = Logger.getLogger("Payment Service");

	public static void error(String message) {
		logger.severe(message);
	}

	public static void info(String message) {
		logger.info(message);
	}
}
