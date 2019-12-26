package com.giftok.payment.processor;

import java.util.Optional;
import java.util.function.Function;

public abstract class ChargeResponse {

	protected String error;

	// represents payments in Payment Gateway
	protected String paymentId;

	public abstract <R> R reduce(Function<String, R> successFunction, Function<String, R> errorFunction);

	public Optional<String> error() {
		return Optional.ofNullable(error);
	}

	public Optional<String> paymentId() {
		return Optional.ofNullable(paymentId);
	}

	public static ChargeResponse failed(String error) {
		return new Failed(error);
	}
	
	public static ChargeResponse success(String paymentId) {
		return new Success(paymentId);
	}
	
	static class Failed extends ChargeResponse {

		public Failed(String error) {
			this.error = error;
		}

		@Override
		public <R> R reduce(Function<String, R> successFunction, Function<String, R> errorFunction) {
			// TODO Auto-generated method stub
			return errorFunction.apply(error);
		}
	}

	static class Success extends ChargeResponse {

		public Success(String paymentId) {
			this.paymentId = paymentId;
		}

		@Override
		public <R> R reduce(Function<String, R> successFunction, Function<String, R> errorFunction) {
			// TODO Auto-generated method stub
			return successFunction.apply(paymentId);
		}
	}
}
