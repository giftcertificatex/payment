package com.giftok.payment;

import java.math.BigDecimal;

/**
 * Represents Reqeust to the Payment Gateway
 * 
 * @author dmytro.tyshchenko
 *
 */
public class ChargeRequest {

	private final String cardToken;
	private final BigDecimal amount;

	public ChargeRequest(String cardToken, BigDecimal amount) {
		this.cardToken = cardToken;
		this.amount = amount;
	}

	public String cardToken() {
		return this.cardToken;
	}

	public BigDecimal amount() {
		return this.amount;
	}
}
