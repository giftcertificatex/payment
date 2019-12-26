package com.giftok.payment.firestore;

import java.util.HashMap;
import java.util.function.Function;

import com.giftok.payment.processor.ChargeResponse;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class FirestorePaymentRepository implements PaymentRespository {

	private final Firestore db;

	private static final String collectionName = "payments-info";

	private final String projectId = "single-outrider-260808";

	public FirestorePaymentRepository() {
		FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId(projectId)
				.build();
		this.db = firestoreOptions.getService();

	}

	public Function<ChargeResponse, Boolean> saveChargeResponse(String certificateId) {
		return chargeResponse -> {
			var map = Operations.createMap(chargeResponse);
			db.collection(collectionName).document(certificateId).create(map);
			return true;
		};
	}

	public static class Operations {

		private static Function<String, HashMap<String, Object>> successFunction = value -> {
			var result = new HashMap<String, Object>();
			result.put(Keys.paymentId.toString(), value);
			return result;
		};
		private static Function<String, HashMap<String, Object>> errorFunction = value -> {
			var result = new HashMap<String, Object>();
			result.put(Keys.error.toString(), value);
			return result;
		};

		static HashMap<String, Object> createMap(ChargeResponse chargeResponse) {
			return chargeResponse.reduce(successFunction, errorFunction);
		}
	}

	public static enum Keys {
		paymentId, error;
	}
}
