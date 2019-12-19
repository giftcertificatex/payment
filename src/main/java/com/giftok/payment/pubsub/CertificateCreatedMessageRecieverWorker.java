package com.giftok.payment.pubsub;

import java.util.concurrent.BlockingQueue;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.giftok.payment.LogUtility;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;

public class CertificateCreatedMessageRecieverWorker implements Runnable {

	private static final String subscriptionId = "payment-service-certificate-created";
	private static final String projectId = "single-outrider-260808";

	private final BlockingQueue<CertificateMessage> queue;

	public CertificateCreatedMessageRecieverWorker(BlockingQueue<CertificateMessage> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		var subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);

		var messageReciever = new CertificateCreatedMessageReceiver(this.queue);

		var subscriber = Subscriber.newBuilder(subscriptionName, messageReciever).build();
		LogUtility.info("Starting listening Certificate Created Message from Pub/Sub...");
		subscriber.startAsync();

		// Wait for termination
		subscriber.awaitTerminated();
	}

}