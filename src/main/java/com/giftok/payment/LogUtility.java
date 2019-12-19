package com.giftok.payment;

import java.util.logging.Logger;

/**
 * Encapsulates logging operations  
 * 
 * @author dmytro.tyshchenko
 *
 */

public class LogUtility {

	public static void error(String message, Class<?> clazz) {
		var logger =  Logger.getLogger(clazz.getName());
		logger.severe(message);
	}

	public static void info(String message, Class<?> clazz) {
		var logger =  Logger.getLogger(clazz.getName());
		logger.info(message);
	}
}
