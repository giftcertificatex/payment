package com.giftof.payment.firestore;

import org.junit.jupiter.api.Test;

import com.giftok.payment.firestore.FirestorePaymentRepository;
import com.giftok.payment.processor.ChargeResponse;
import com.google.cloud.firestore.FirestoreOptions;


public class FirestorePaymentRepositoryTestIT {

	@Test
	public void shouldSaveInPaymentRepository() throws Exception {
		var paymentRespository = new FirestorePaymentRepository();
		var chargeResponse = ChargeResponse.success("somePaymentId");
		paymentRespository.saveChargeResponse("1234567").apply(chargeResponse);
		//delete results
		var projectId = "single-outrider-260808";
		var firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId(projectId)
				.build();
		var db = firestoreOptions.getService();
		db.collection("payments-info").document("1234567").delete().get();

	}
}
