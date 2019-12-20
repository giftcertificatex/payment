package com.giftok.payment.pubsub;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import org.junit.jupiter.api.Test;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;


public class CertificateCreatedMessageRecieverTestIT {
	
	
	@Test
	public void shouldRecieveCertificateCreatedMessage() throws Exception {
	
		//publidsh Certificate Created Message
		var topicId = "certificate-created-topic";
		var subscriptionId = "payment-service-certificate-created";
		var projectId = "single-outrider-260808";
		
		var topicName = ProjectTopicName.of(projectId, topicId);
		
		var certificateMessage = CertificateMessage.newBuilder().setId("testCert").setAmount(500).build();

		var pubSubMessage = PubsubMessage.newBuilder().setData(certificateMessage.toByteString()).build();

		
		var publisher = Publisher.newBuilder(topicName).build();
		var futureRes = publisher.publish(pubSubMessage);
		var messageID = futureRes.get();
		publisher.shutdown();
		
		var queue = new LinkedBlockingDeque<CertificateMessage>();

		//listening for Certificate Massage Queue
		var receiver = new CertificateCreatedMessageRecieverWorker(queue);
		
		var executorService = Executors.newSingleThreadExecutor();
		
		executorService.submit(receiver);
		
		//waiting for Message here
		var messageFromPubSub = queue.take();
		System.out.println(messageFromPubSub.toString());
		assertEquals(messageFromPubSub.getId(), "testCert");
	}
}
