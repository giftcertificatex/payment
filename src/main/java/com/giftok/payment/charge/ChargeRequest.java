package com.giftok.payment.charge;

/**
 * Represents Reqeust to the Payment Gateway
 * 
 * @author dmytro.tyshchenko
 *
 */
public class ChargeRequest {

	private final String cardToken;
	//amount is int because it in cents
	private final int amount;

	public ChargeRequest(String cardToken, int amount) {
		this.cardToken = cardToken;
		this.amount = amount;
	}

	public String cardToken() {
		return this.cardToken;
	}

	public int amount() {
		return this.amount;
	}
}
