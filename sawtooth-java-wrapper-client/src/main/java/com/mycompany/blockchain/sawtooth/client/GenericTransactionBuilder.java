package com.mycompany.blockchain.sawtooth.client;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;

import sawtooth.sdk.client.Signing;
import sawtooth.sdk.processor.Utils;
import sawtooth.sdk.protobuf.Transaction;
import sawtooth.sdk.protobuf.TransactionHeader;

public abstract class GenericTransactionBuilder<ENTITY>{
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
	 * @throws Exception 
	 */
	TransactionHeaderDTO buildTransaction(ENTITY payload) throws Exception {

		String payloadBytes = Utils.hash512(getEncodedPayload(payload));  //--fix for invaluid payload seriqalization
        ByteString payloadByteString  = ByteString.copyFrom(getEncodedPayload(payload));
        
		String publicKeyHex = signer.getSignerPrivateKey().getPublicKeyAsHex();
		//@formatter:off
		TransactionHeader txnHeader = TransactionHeader.newBuilder()
				.clearBatcherPublicKey()
				.setBatcherPublicKey(publicKeyHex)
				.setFamilyName(iAddressBuilder.getTransactionFamilyName())  
				.setFamilyVersion(iAddressBuilder.getTransactionFamilyVersion())
				//.addInputs(iAddressBuilder.buildAddress(payload))
				.addInputs("1cf1264aa624fa573079918f86c958f503cecb210ec2b258092079105096dbbdd61976")
				.setNonce("1")
				//.addOutputs(iAddressBuilder.buildAddress(payload))
				.addOutputs("1cf1264aa624fa573079918f86c958f503cecb210ec2b258092079105096dbbdd61976")
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
		TransactionHeaderDTO txnHeaderDTO = new TransactionHeaderDTO(txn, headerSignature);
		//@formatter:on
		return txnHeaderDTO;
	}
	

	protected abstract byte[] getEncodedPayload(ENTITY payload) throws Exception;


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