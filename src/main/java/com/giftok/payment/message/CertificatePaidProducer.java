package com.giftok.payment.message;

import com.giftok.payment.charge.ChargeResponse;

public interface CertificatePaidProducer {

	void publish(String certificateId, ChargeResponse chargeResult); 
}
