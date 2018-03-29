package com.mycompany.blockchain.sawtooth.client.string;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;

import sawtooth.sdk.client.Signing;
import sawtooth.sdk.processor.Utils;
import sawtooth.sdk.protobuf.Transaction;
import sawtooth.sdk.protobuf.TransactionHeader;

class GenericTransactionBuilder{
	/**
	 * The signer to signe batch and transactions
	 */
	Signer signer;
	
	/**
	 * The address builder to buidl addresses for entites
	 */
	IAddressBuilder<String> iAddressBuilder;
	
	/**
	 * Builds a transaction for a signer and a payload
	 * @param payload
	 * @return
	 */
	Transaction buildTransaction(String payload) {
		String payloadBytes = Utils.hash512(payload.getBytes());
		ByteString payloadByteString = ByteString.copyFrom(payload.getBytes());
		String publicKeyHex = signer.getSignerPrivateKey().getPublicKeyAsHex();
		//@formatter:off
		TransactionHeader txnHeader = TransactionHeader.newBuilder().clearBatcherPublicKey()
				.setBatcherPublicKey(publicKeyHex)
				.setFamilyName(iAddressBuilder.getTransactionFamilyName())  
				.setFamilyVersion("1.0")
				.addInputs(iAddressBuilder.buildAddress(payload)) 
				.setNonce("1")
				.addOutputs(iAddressBuilder.buildAddress(payload))
				.setPayloadSha512(payloadBytes)
				.setSignerPublicKey(publicKeyHex)
				.build();
		//@formatter:on
		
		ByteString txnHeaderBytes = txnHeader.toByteString();
		String headerSignature = Signing.sign(signer.getSignerPrivateKey(), txnHeader.toByteArray());
		//@formatter:off
		Transaction txn = Transaction.newBuilder()
				.setHeader(txnHeaderBytes)
				.setPayload(payloadByteString)
				.setHeaderSignature(headerSignature)
				.build();
		//@formatter:on
		return txn;
	}
	
}