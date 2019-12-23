package com.giftok.payment.pubsub;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.BlockingQueue;

import org.junit.jupiter.api.Test;

import com.giftok.payment.ioc.PaymentModule;
import com.giftok.payment.message.PaymentMessageOuterClass.PaymentMessage;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;


public class PaymentWorkerTest {

	//@Test
	public void shouldInitializePaymentWorker() throws Exception {
		
		Injector injector = Guice.createInjector(new PaymentModule());
		var paymentWorker = injector.getInstance(Key.get(Runnable.class, Names.named("paymentWorker")));
		assertNotNull(paymentWorker);
	}
}
