package com.giftok.payment;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.giftok.payment.message.PaymentMessageOuterClass.PaymentMessage;
import com.giftok.payment.processor.PaymentGateway;
import com.giftok.payment.processor.PaymentWorker;
import com.giftok.payment.pubsub.CertificateCreatedMessageRecieverWorker;
import com.giftok.payment.pubsub.CertificatePaidPublisherWorker;

/**
 * Orchestrates Payment Flow. 
 * Initializes PaymentGateway. 
 * Creates Workers for listening messages from the "payment-service-certificate-created" topic.
 * Creates Workers to process payments.
 * Creates Workers to publish payments result into "certificate-paid-topic" topic.
 * Manages pool of Threads for each type of Worker. 
 * 
 * Communications between Workers are organized through set of BlockingQueue. 
 * Payment flow is next:
 * CertificateCreatedMessageReceiverWorker is listening for CertificateMessage from "payment-service-certificate-created".
 * After CertificateCreatedMessageReceiverWorker receive CertificateMessage it publish CertificateMessage to 'certificateCreatedQueue' queue.
 * PaymentWorker pulls CertificateMessage from 'certificateCreatedQueue' queue and triggers PaymentProcessor to make a Payment
 * PaymentProcessor makes a payment and publish payment results aka PaymentMessage to the 'paymentMessageQueue' queue.
 * CertificatePaidPublisherWorker pulls PaymentMessage and publishes it to the "certificate-paid-topic". 
 * 
 * @author dmytro.tyshchenko
 *
 */

public class PaymentOrhestrator {

	private final int numberOfCertCreatedThreads = 2;
	private final int numberOfPaymentThreads = 2;
	private final int numberOfPublishPaidThreads = 2;
	
	private final ExecutorService certCreatedExecutorService = Executors.newFixedThreadPool(numberOfCertCreatedThreads);
	private final ExecutorService paymentProcessorExecutorService = Executors.newFixedThreadPool(numberOfPaymentThreads);
	private final ExecutorService publishPaidExecutorService = Executors.newFixedThreadPool(numberOfPublishPaidThreads);

	
	private final PaymentGateway paymentGateway = PaymentGateway.getInstance();

	private final BlockingQueue<CertificateMessage> certificateCreatedQueue = new LinkedBlockingDeque<>();
	private final BlockingQueue<PaymentMessage> paymentMessageQueue = new LinkedBlockingDeque<>();

	public PaymentOrhestrator() {

	}

	public void start() {

		repeate(numberOfCertCreatedThreads, () -> {
			certCreatedExecutorService.submit(new CertificateCreatedMessageRecieverWorker(certificateCreatedQueue));
		});

		repeate(numberOfPaymentThreads, () -> {
			paymentProcessorExecutorService
					.submit(new PaymentWorker(paymentGateway, paymentMessageQueue, certificateCreatedQueue));
		});

		repeate(numberOfPublishPaidThreads, () -> {
			publishPaidExecutorService.submit(new CertificatePaidPublisherWorker(paymentMessageQueue));
		});

	}

	private void repeate(int numberOfrepeats, Runnable operation) {
		for (int i = 0; i < numberOfrepeats; i++) {
			operation.run();
		}

	}
}
