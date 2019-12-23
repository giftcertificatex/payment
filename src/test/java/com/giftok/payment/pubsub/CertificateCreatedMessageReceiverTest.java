package com.giftok.payment.pubsub;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.LinkedBlockingDeque;

import org.junit.jupiter.api.Test;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.pubsub.v1.PubsubMessage;

public class CertificateCreatedMessageReceiverTest {

	@Test
	public void shouldReceiveMessage() throws Exception {
		var queue = new LinkedBlockingDeque<CertificateMessage>();
		var receiver = new CertificateCreatedMessageReceiver(queue);
		var message = PubsubMessage.newBuilder().build();
		receiver.receiveMessage(message, new MyReplayConsumer());
		var receivedMessage = queue.take();
		assertNotNull(receivedMessage);
	}

	static class MyReplayConsumer implements AckReplyConsumer {

		@Override
		public void ack() {
			// TODO Auto-generated method stub

		}

		@Override
		public void nack() {
			// TODO Auto-generated method stub

		}

	}
}
