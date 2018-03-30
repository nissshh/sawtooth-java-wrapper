package com.mycompany.blockchain.sawtooth.client.string;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;

import sawtooth.sdk.client.Signing;
import sawtooth.sdk.processor.Utils;
import sawtooth.sdk.protobuf.Transaction;
import sawtooth.sdk.protobuf.TransactionHeader;

class GenericTransactionBuilder<ENTITY>{
	/**
	 * The signer to signe batch and transactions
	 */
	Signer signer;
	
	/**
	 * The address builder to buidl addresses for entites
	 */
	IAddressBuilder<ENTITY> iAddressBuilder;
	
	/**
	 * Builds a transaction for a signer and a payload
	 * @param payload
	 * @return
	 */
	Transaction buildTransaction(ENTITY payload) {
		String payloadBytes = Utils.hash512(payload.toString().getBytes());
		ByteString payloadByteString = ByteString.copyFrom(payload.toString().getBytes()); //TODO for protos to take from Bytes or serialized bytes.
		String publicKeyHex = signer.getSignerPrivateKey().getPublicKeyAsHex();
		//@formatter:off
		TransactionHeader txnHeader = TransactionHeader.newBuilder().clearBatcherPublicKey()
				.setBatcherPublicKey(publicKeyHex)
				.setFamilyName(iAddressBuilder.getTransactionFamilyName())  
				.setFamilyVersion(iAddressBuilder.getTransactionFamilyVersion())
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

	public Signer getSigner() {
		return signer;
	}

	public void setSigner(Signer signer) {
		this.signer = signer;
	}

	public IAddressBuilder<ENTITY> getiAddressBuilder() {
		return iAddressBuilder;
	}

	public void setiAddressBuilder(IAddressBuilder<ENTITY> iAddressBuilder) {
		this.iAddressBuilder = iAddressBuilder;
	}
	
}