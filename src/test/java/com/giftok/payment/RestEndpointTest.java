package com.giftok.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

//https://stackoverflow.com/questions/47346003/how-to-add-protobuf-file-for-topic-in-gcloud-pubsub

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
	
	@Test
	public void someTest() throws Exception {
		 String messageStr = RestEndpoint.getMessage.apply(jsonMessage);
		 Gson gson = new Gson();
		 PubsubMessage message = gson.fromJson(messageStr, PubsubMessage.class);
		 ByteString byteString  = message.getData();
		 System.out.println(byteString.toString());
	}
}
