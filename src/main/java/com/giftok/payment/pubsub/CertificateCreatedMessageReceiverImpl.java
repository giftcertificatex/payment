package com.giftok.payment.pubsub;

import java.util.concurrent.BlockingQueue;

import com.giftok.certeficate.message.CertificateMessageOuterClass.CertificateMessage;
import com.giftok.payment.LogUtility;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.pubsub.v1.PubsubMessage;

public class CertificateCreatedMessageReceiverImpl implements MessageReceiver {

	private final BlockingQueue<CertificateMessage> queue;

	public CertificateCreatedMessageReceiverImpl(BlockingQueue<CertificateMessage> queue) {
		this.queue = queue;
	}

	@Override
	public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
		try {
			var certeficateMessage = createCertificateMessage(message);
			this.queue.put(certeficateMessage);
			consumer.ack();
		} catch (Exception e) {
			LogUtility.error(e.getMessage());
			consumer.nack();
		}
	}

	private CertificateMessage createCertificateMessage(PubsubMessage pubSubMessage)
			throws InvalidProtocolBufferException {
		return CertificateMessage.parseFrom(pubSubMessage.getData());
	}
}
