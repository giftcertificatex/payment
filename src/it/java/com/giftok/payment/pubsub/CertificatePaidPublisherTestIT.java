package com.giftok.payment.pubsub;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.giftok.payment.message.PaymentMessageOuterClass.PaymentMessage;


public class CertificatePaidPublisherTestIT {

	
	@Test
	public void shouldPublishPaidMessage() throws Exception {
		
		var publisher = new CertificatePaidPublisher();
		var message = PaymentMessage.newBuilder().setCerteficateId("testID").build();
		var result = publisher.publish(message);
		//If result is Empty it will throw Exception
		System.out.println(result.get());
	}
}
