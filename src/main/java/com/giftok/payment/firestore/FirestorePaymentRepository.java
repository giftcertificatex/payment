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
			var map = createMap(chargeResponse);
			db.collection(collectionName).document(certificateId).create(map);
			return true;
		};
	}

	private HashMap<String, Object> createMap(ChargeResponse chargeResponse) {
		var result = new HashMap<String, Object>();
		chargeResponse.error().map(error -> result.put("error", error));
		chargeResponse.paymentId().map(paymentId -> result.put("paymentId", paymentId));
		return result;
	}

}
