
/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.giftok.payment;

import com.giftok.payment.ioc.PaymentModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Main class of Payment Service. Starts Payment Orhestrator and HealthEnpoint
 * Initializes IoC 
 * @See PaymentOrhestrator for detailed description of Payment Process
 * 
 * @author dmytro.tyshchenko
 *
 */

public class Application {

	public static void main(String[] args) {
		
		Injector injector = Guice.createInjector(new PaymentModule());
		
		HealthEndpoint.startListening();
		var paymnetOrhestartor = new PaymentOrhestrator(injector);
		paymnetOrhestartor.start();
	}
}
