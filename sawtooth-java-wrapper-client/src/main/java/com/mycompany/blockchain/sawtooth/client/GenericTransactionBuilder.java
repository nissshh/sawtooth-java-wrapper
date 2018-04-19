package com.mycompany.blockchain.sawtooth.client;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;

import sawtooth.sdk.client.Signing;
import sawtooth.sdk.processor.Utils;
import sawtooth.sdk.protobuf.Transaction;
import sawtooth.sdk.protobuf.TransactionHeader;

/**
 * A generic transaction builder that will build a transaction for a given ENTITY and PAYLOAD.The class
 * higes the semantics for sending a tranaction to the Sawtooth TP.
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 * @param <ENTITY>  Defines the entity/data that will be store on the blockchain
 * @param <PAYLOAD> Defines the payload that will be usign to send to TP.
 */
public abstract class GenericTransactionBuilder<ENTITY,PAYLOAD>{
	/**
	 * The signer to signer batch and transactions
	 */
	Signer signer;
	
	/**
	 * The address builder to buidl addresses for entites
	 */
	IAddressBuilder<ENTITY> iAddressBuilder;
	
	/**
	 * Builds a transaction for a signer and a payload
	 * @param payload the payload that contain the entity to be sent and persisted on blockchain.
	 * @return
	 * @throws Exception 
	 */
	public TransactionHeaderDTO buildTransaction(PAYLOAD payload) throws Exception {

		String payloadBytes = Utils.hash512(getEncodedPayload(payload)); 
        ByteString payloadByteString  = ByteString.copyFrom(getEncodedPayload(payload));
        
		String publicKeyHex = signer.getSignerPrivateKey().getPublicKeyAsHex();
		
		ENTITY entity = getEntity(payload);//extract the entity 
		
		//@formatter:off
		TransactionHeader txnHeader = TransactionHeader.newBuilder()
				.clearBatcherPublicKey()
				.setBatcherPublicKey(publicKeyHex)
				.setFamilyName(iAddressBuilder.getTransactionFamilyName())  
				.setFamilyVersion(iAddressBuilder.getTransactionFamilyVersion())
				.addInputs(iAddressBuilder.buildAddress(entity))
				.setNonce("1")
				.addOutputs(iAddressBuilder.buildAddress(entity))
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
	
	/**
	 * The encoded  or un encoded (simple bytes) form for the whole payload.In case there is encoding is use das part of  sendign the data.
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	protected abstract byte[] getEncodedPayload(PAYLOAD payload) throws Exception;

	/**
	 * Get the entity from payload.Since entities are used for calculating the address. The data inside the entity defines teh key that is used
	 * The entity data defines the addressing mechanism that is used. Entity is part of Payload
	 * @param payload
	 * @return
	 */
	protected abstract ENTITY getEntity(PAYLOAD payload);

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