package com.giftok.payment.processor;

import java.util.concurrent.BlockingQueue;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.giftok.payment.LogUtility;
import com.giftok.payment.message.PaymentMessageOuterClass.PaymentMessage;

public class PaymentWorker implements Runnable {

	private final PaymentProcessor paymentProcessor;

	private final BlockingQueue<CertificateMessage> certificateCreatedQueue;

	public PaymentWorker(PaymentGateway paymentGateway, BlockingQueue<PaymentMessage> paymentMessageQueue,
			BlockingQueue<CertificateMessage> certificateCreatedQueue) {
		this.paymentProcessor = new PaymentProcessor(paymentGateway, paymentMessageQueue);
		this.certificateCreatedQueue = certificateCreatedQueue;
	}

	@Override
	public void run() {
		LogUtility.info("Payment Worker is listening for Certificate Message");
		while (true) {
			try {
				var certificateMessage = certificateCreatedQueue.take();
				paymentProcessor.process(certificateMessage);
			} catch (InterruptedException e) {
				LogUtility.error("Can't take Certificate Message from CertificateCreatedQueue ");
			}
		}
	}

}
