package com.giftok.payment.message;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.giftok.payment.charge.ChargeResponse;

public interface CerftificateCreatedConsumer {

	ChargeResponse processMessage(CertificateMessage message);
}
