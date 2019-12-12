package com.giftok.payment;

import java.util.Optional;

public class ChargeResponse {

	private String errorCode;

	public Optional<String> errorCode() {
		return Optional.ofNullable(errorCode);
	}

	public void setErrorCode(String errorCode) {

		this.errorCode = errorCode;
	}
}
