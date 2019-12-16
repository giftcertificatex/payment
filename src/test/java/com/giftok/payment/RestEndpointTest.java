package com.giftok.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RestEndpointTest {

	private static final String jsonMessage = "{"+
		     "\"message\": {"+
	       "\"attributes\": {"+
	         "\"key\": \"value\""+
	       "},"+
	       "\"data\": \"SGVsbG8gQ2xvdWQgUHViL1N1YiEgSGVyZSBpcyBteSBtZXNzYWdlIQ==\","+
	       "\"messageId\": \"136969346945\""+
	     "},"+
	     "\"subscription\": \"projects/myproject/subscriptions/mysubscription\""+
	   "}";
	
	@Test
	public void shouldExtractMessageData() throws Exception {
		var result = RestEndpoint.getMessage.andThen(RestEndpoint.getData).apply(jsonMessage);
		assertEquals(result, "\"SGVsbG8gQ2xvdWQgUHViL1N1YiEgSGVyZSBpcyBteSBtZXNzYWdlIQ==\"");
	}
}
