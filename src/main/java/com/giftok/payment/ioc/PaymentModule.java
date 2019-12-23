package com.giftok.payment.ioc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Supplier;

import javax.inject.Inject;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.giftok.payment.message.PaymentMessageOuterClass.PaymentMessage;
import com.giftok.payment.processor.PaymentGateway;
import com.giftok.payment.processor.PaymentProcessor;
import com.giftok.payment.processor.PaymentWorker;
import com.giftok.payment.processor.stripe.StripePaymentGateway;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.giftok.payment.pubsub.CertificateCreatedMessageReceiver;
import com.giftok.payment.pubsub.CertificateCreatedMessageRecieverWorker;
import com.giftok.payment.pubsub.CertificatePaidPublisherWorker;

public class PaymentModule extends AbstractModule {

	private final BlockingQueue<PaymentMessage> paymentMessageQueue = new LinkedBlockingDeque<>();
	private final BlockingQueue<CertificateMessage> certificateCreatedQueue = new LinkedBlockingDeque<CertificateMessage>();

	@Override
	protected void configure() {

		// maybe usefull for testing
		bind(BlockingQueue.class).annotatedWith(Names.named("paymentMessageQueue")).toInstance(paymentMessageQueue);

		bind(BlockingQueue.class).annotatedWith(Names.named("certificateCreatedQueue"))
				.toInstance(certificateCreatedQueue);

		// Payment Gateway
		bind(PaymentGateway.class).to(StripePaymentGateway.class);

		// Workers
		bind(Runnable.class).annotatedWith(Names.named("paymentWorker")).to(PaymentWorker.class);

		bind(Runnable.class).annotatedWith(Names.named("certificatePaidPublisherWorker"))
				.to(CertificatePaidPublisherWorker.class);

		bind(MessageReceiver.class).annotatedWith(Names.named("certificateCreatedMessageReceiver"))
				.to(CertificateCreatedMessageReceiver.class);

		bind(Runnable.class).annotatedWith(Names.named("certificateCreatedMessageRecieverWorker"))
				.to(CertificateCreatedMessageRecieverWorker.class);

	}

	@Provides
	PaymentProcessor paymentProcessor(PaymentGateway paymentGateway) {
		return new PaymentProcessor(paymentGateway, this.paymentMessageQueue);
	}

	@Provides
	PaymentWorker paymentWorker(PaymentProcessor paymentProcessor) {
		return new PaymentWorker(paymentProcessor, this.certificateCreatedQueue);
	}

	@Provides
	CertificateCreatedMessageReceiver certificateCreatedMessageReceiver() {
		return new CertificateCreatedMessageReceiver(this.certificateCreatedQueue);
	}

	@Provides
	@Inject
	CertificateCreatedMessageRecieverWorker certificateCreatedMessageRecieverWorker(
			CertificateCreatedMessageReceiver messageReceiver) {
		return new CertificateCreatedMessageRecieverWorker(new Supplier<MessageReceiver>() {

			@Override
			public MessageReceiver get() {
				return messageReceiver;
			}

		});
	}

	@Provides
	CertificatePaidPublisherWorker certificatePaidPublisherWorker() {
		return new CertificatePaidPublisherWorker(this.paymentMessageQueue);
	}

}
