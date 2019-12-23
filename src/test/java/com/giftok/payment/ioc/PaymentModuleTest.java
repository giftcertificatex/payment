package com.giftok.payment.ioc;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.BlockingQueue;

import org.junit.jupiter.api.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class PaymentModuleTest {

	@Test
	public void shouldInitializeDependencies() throws Exception {

		Injector injector = Guice.createInjector(new PaymentModule());

		var paymentQueue = injector.getInstance(Key.get(BlockingQueue.class, Names.named("paymentMessageQueue")));
		assertNotNull(paymentQueue);

		var certificateCreatedQueue = injector
				.getInstance(Key.get(BlockingQueue.class, Names.named("certificateCreatedQueue")));
		assertNotNull(certificateCreatedQueue);

		var paymentWorker = injector.getInstance(Key.get(Runnable.class, Names.named("paymentWorker")));
		assertNotNull(paymentWorker);

		var certificateCreatedMessageRecieverWorker = injector
				.getInstance(Key.get(Runnable.class, Names.named("certificateCreatedMessageRecieverWorker")));
		assertNotNull(certificateCreatedMessageRecieverWorker);

		var certificatePaidPublisherWorker = injector
				.getInstance(Key.get(Runnable.class, Names.named("certificatePaidPublisherWorker")));
		assertNotNull(certificatePaidPublisherWorker);

	}

}
