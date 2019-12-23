package com.giftok.payment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * Orchestrates Payment Flow. Initializes PaymentGateway. Creates Workers for
 * listening messages from the "payment-service-certificate-created" topic.
 * Creates Workers to process payments. Creates Workers to publish payments
 * result into "certificate-paid-topic" topic. Manages pool of Threads for each
 * type of Worker.
 * 
 * Communications between Workers are organized through set of BlockingQueue.
 * Payment flow is next: CertificateCreatedMessageReceiverWorker is listening
 * for CertificateMessage from "payment-service-certificate-created". After
 * CertificateCreatedMessageReceiverWorker receive CertificateMessage it publish
 * CertificateMessage to 'certificateCreatedQueue' queue. PaymentWorker pulls
 * CertificateMessage from 'certificateCreatedQueue' queue and triggers
 * PaymentProcessor to make a Payment PaymentProcessor makes a payment and
 * publish payment results aka PaymentMessage to the 'paymentMessageQueue'
 * queue. CertificatePaidPublisherWorker pulls PaymentMessage and publishes it
 * to the "certificate-paid-topic".
 * 
 * @author dmytro.tyshchenko
 *
 */

public class PaymentOrhestrator {

	private final int numberOfCertCreatedThreads = 2;
	private final int numberOfPaymentThreads = 2;
	private final int numberOfPublishPaidThreads = 2;

	private final ExecutorService certCreatedExecutorService = Executors.newFixedThreadPool(numberOfCertCreatedThreads);
	private final ExecutorService paymentProcessorExecutorService = Executors
			.newFixedThreadPool(numberOfPaymentThreads);
	private final ExecutorService publishPaidExecutorService = Executors.newFixedThreadPool(numberOfPublishPaidThreads);

	private final Injector injector;

	public PaymentOrhestrator(Injector injector) {
		this.injector = injector;
	}

	public void start() {

		repeate(numberOfCertCreatedThreads, () -> {
			certCreatedExecutorService.submit(injector
					.getInstance(Key.get(Runnable.class, Names.named("certificateCreatedMessageRecieverWorker"))));
		});

		repeate(numberOfPaymentThreads, () -> {
			paymentProcessorExecutorService
					.submit(injector.getInstance(Key.get(Runnable.class, Names.named("paymentWorker"))));
		});

		repeate(numberOfPublishPaidThreads, () -> {
			publishPaidExecutorService.submit(
					injector.getInstance(Key.get(Runnable.class, Names.named("certificatePaidPublisherWorker"))));
		});

	}

	private void repeate(int numberOfrepeats, Runnable operation) {
		for (int i = 0; i < numberOfrepeats; i++) {
			operation.run();
		}

	}
}
