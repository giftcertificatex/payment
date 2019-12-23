package com.giftok.payment.processor;

import java.util.concurrent.BlockingQueue;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.giftok.payment.LogUtility;

public class PaymentWorker implements Runnable {

	private final PaymentProcessor paymentProcessor;

	private final BlockingQueue<CertificateMessage> certificateCreatedQueue;

	public PaymentWorker(PaymentProcessor paymentProcessor,
			BlockingQueue<CertificateMessage> certificateCreatedQueue) {
		this.paymentProcessor = paymentProcessor;
		this.certificateCreatedQueue = certificateCreatedQueue;
	}

	@Override
	public void run() {
		LogUtility.info("Payment Worker is listening for Certificate Message", this.getClass());
		while (true) {
			try {
				var certificateMessage = certificateCreatedQueue.take();
				paymentProcessor.process(certificateMessage);
			} catch (InterruptedException e) {
				LogUtility.error("Can't take Certificate Message from CertificateCreatedQueue ", this.getClass());
			}
		}
	}
}
