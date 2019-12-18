package com.giftok.payment;

import java.util.Collections;

import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.logging.Payload.StringPayload;
import com.google.cloud.logging.Severity;

public class LogUtility {
	
	private static final Logging logging = LoggingOptions.getDefaultInstance().getService();

	public static void error(String message) {
		
		LogEntry entry = LogEntry.newBuilder(StringPayload.of(message))
		        .setSeverity(Severity.ERROR).build();
		
		logging.write(Collections.singleton(entry));
	}
	
	public static void debug(String message) {
		
		LogEntry entry = LogEntry.newBuilder(StringPayload.of(message))
		        .setSeverity(Severity.DEBUG)
		        .build();
		
		logging.write(Collections.singleton(entry));
	}
}
