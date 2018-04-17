package com.mycompany.blockchain.sawtooth.client.payment;

import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Payment;
import com.mycompany.blockchain.sawtooth.loan.protobuf.PaymentPayload;

public class PaymentTransactionBuilder extends GenericTransactionBuilder<Payment, PaymentPayload> {

	@Override
	protected byte[] getEncodedPayload(PaymentPayload payload) throws Exception {
		return payload.toByteArray(); // no encoding used.
	}

	@Override
	protected Payment getEntity(PaymentPayload payload) {
		if (payload.hasPayment()) {
			return  payload.getPayment();			
		}  else {
			return null;
		}
	}
}
