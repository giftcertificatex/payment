package com.giftok.payment.message;

import com.google.pubsub.v1.PubsubMessage;

public interface CerftificateCreatedConsumer {

	void processMessage(PubsubMessage message);}
