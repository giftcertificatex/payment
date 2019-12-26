package com.giftok.payment.firestore;

import java.util.HashMap;
import java.util.function.Function;

import com.giftok.payment.LogUtility;
import com.giftok.payment.processor.ChargeResponse;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class FirestorePaymentRepository implements PaymentRepository {

	private final Firestore db;

	private static final String collectionName = "payments-info";

	private final String projectId = "single-outrider-260808";

	public FirestorePaymentRepository() {
		FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId(projectId)
				.build();
		this.db = firestoreOptions.getService();

	}

	@Override
	public Function<ChargeResponse, Boolean> saveChargeResponse(String certificateId) {
		return chargeResponse -> {
			var map = Operations.createMap(chargeResponse);
			var collectionRef = db.collection(collectionName);
			var docRef = collectionRef.document(certificateId);
			try {
				//TODO or create?
				docRef.set(map).get();
			} catch (Exception e) {
				/**
				 * We don't throw Exception here. 
				 * If we have problem with saving Payment Results in DB we should not break process.
				 * At this point Charge can be successfully completed so we can't raise Exception
				 */
				System.err.println(e.getMessage());
				LogUtility.error(certificateId + " payment Info was not Saved in DB " + e.getMessage()+chargeResponse.toString(),
						FirestorePaymentRepository.class);
			}
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
