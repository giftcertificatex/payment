package com.giftok.payment.ioc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Supplier;

import javax.inject.Inject;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.giftok.payment.LogUtility;
import com.giftok.payment.firestore.FirestorePaymentRepository;
import com.giftok.payment.firestore.PaymentRepository;
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

	private final String stripeApiKey;// = "sk_test_6kK5at10gSmpC7v9XJHhkxgB00r6oYzklj";

	private final BlockingQueue<PaymentMessage> paymentMessageQueue = new LinkedBlockingDeque<>();
	private final BlockingQueue<CertificateMessage> certificateCreatedQueue = new LinkedBlockingDeque<CertificateMessage>();

	public PaymentModule() {
		this.stripeApiKey = System.getenv().getOrDefault("STRIPE_API_KEY", "haha");
		LogUtility.info("Read Stripe Api Key: " + this.stripeApiKey, PaymentModule.class);
	}

	@Override
	protected void configure() {

		// maybe usefull for testing
		bind(BlockingQueue.class).annotatedWith(Names.named("paymentMessageQueue")).toInstance(paymentMessageQueue);

		bind(BlockingQueue.class).annotatedWith(Names.named("certificateCreatedQueue"))
				.toInstance(certificateCreatedQueue);

		// Payment Gateway
		bind(PaymentGateway.class).to(StripePaymentGateway.class);

		// Repository
		bind(PaymentRepository.class).to(FirestorePaymentRepository.class);

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
	StripePaymentGateway stripePaymentGateway() {
		return new StripePaymentGateway(stripeApiKey);
	}

	@Provides
	PaymentProcessor paymentProcessor(PaymentGateway paymentGateway, PaymentRepository paymentRepository) {
		return new PaymentProcessor(paymentGateway, this.paymentMessageQueue, paymentRepository);
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
