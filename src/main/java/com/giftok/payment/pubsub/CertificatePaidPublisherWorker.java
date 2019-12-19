package com.giftok.payment.pubsub;

import java.util.concurrent.BlockingQueue;

import com.giftok.payment.LogUtility;
import com.giftok.payment.message.PaymentMessageOuterClass.PaymentMessage;

public class CertificatePaidPublisherWorker implements Runnable {

	private final BlockingQueue<PaymentMessage> paymentMessageQueue;

	public CertificatePaidPublisherWorker(BlockingQueue<PaymentMessage> paymentMessageQueue) {

		this.paymentMessageQueue = paymentMessageQueue;
	}

	@Override
	public void run() {
		LogUtility.info("Paid Publisher Worker is listening for Payment Message");
		while (true) {
			try {
				var message = paymentMessageQueue.take();
				var publisher = new CertificatePaidPublisher();
				publisher.publish(message);
			} catch (InterruptedException e) {
				LogUtility.error("Can't take Payment Message");
			}
		}
	}

}
