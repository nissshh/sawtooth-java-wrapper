package com.mycompany.blockchain.sawtooth.client.string;

import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;

public class StringTransactionBuilder extends GenericTransactionBuilder<String,String> {

	@Override
	protected byte[] getEncodedPayload(String payload) throws Exception {
		return payload.toString().getBytes();
	}

	@Override
	protected String getEntity(String payload) {
		return payload;
	}

}
