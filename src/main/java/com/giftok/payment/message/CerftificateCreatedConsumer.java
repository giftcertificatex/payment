package com.giftok.payment.message;

import com.giftok.payment.charge.ChargeResponse;
import com.google.pubsub.v1.PubsubMessage;

public interface CerftificateCreatedConsumer {

	ChargeResponse processMessage(PubsubMessage message);
}
