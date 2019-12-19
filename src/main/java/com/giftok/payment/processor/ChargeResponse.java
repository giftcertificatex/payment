package com.giftok.payment.processor;

import java.util.Optional;

public class ChargeResponse {

	private String error;

	public Optional<String> error() {
		return Optional.ofNullable(error);
	}

	public void setError(String error) {
		this.error = error;
	}
}
