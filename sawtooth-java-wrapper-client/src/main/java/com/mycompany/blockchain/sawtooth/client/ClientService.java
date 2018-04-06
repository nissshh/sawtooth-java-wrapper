package com.mycompany.blockchain.sawtooth.client;

import java.util.logging.Logger;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;

import sawtooth.sdk.protobuf.BatchList;

/**
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 * @param <ENTITY>  Entity that will be stored on block chaing. The concreate implementation define what entity they will work on and further defines the 
 * adderssing and entities that are used.
 * @param <PAYLOAD> Payload that will be sent to Transaction Family.Further forms the encoded payuloa. A payload generally contains Entity and Action to be permformed
 * on the entitye. 
 */
public abstract class ClientService<ENTITY,PAYLOAD> {

	private static Logger logger = Logger.getLogger(ClientService.class.getName());
	
	private String txFamily;
	
	private String txVersion;
	
	private String signerKey;

	protected ClientEndpointService clientService;
	
	protected GenericBatchBuilder batchBuilder;
	
	protected GenericTransactionBuilder<ENTITY,PAYLOAD> transactionBuilder;
	
	protected Signer signer;
	
	protected IAddressBuilder<ENTITY> iAddressBuilder;

	public ClientService(String txFamily, String txVersion, String signerKey) {
		this.txFamily = txFamily;
		this.txVersion = txVersion;
		this.signerKey = signerKey;
	}

	
	public void init() {
		signer = new Signer(signerKey);
		transactionBuilder.setSigner(signer);
		transactionBuilder.setiAddressBuilder(iAddressBuilder);
		batchBuilder= new GenericBatchBuilder();
		batchBuilder.setSigner(signer);
		clientService = new ClientEndpointService();
	}


	public String service(PAYLOAD payload) throws Exception {
		TransactionHeaderDTO transaction = transactionBuilder.buildTransaction(payload);
		BatchList batch = batchBuilder.buildBatch(transaction);
		ByteString batchBytes = batch.toByteString();
		String response = clientService.submit(batchBytes);
		logger.info("Response for submission is : "+ response);
		return response;
		
	}

	public void destroy() {

	}


	public String getTxFamily() {
		return txFamily;
	}


	public void setTxFamily(String txFamily) {
		this.txFamily = txFamily;
	}


	public String getTxVersion() {
		return txVersion;
	}


	public void setTxVersion(String txVersion) {
		this.txVersion = txVersion;
	}


	public String getSignerKey() {
		return signerKey;
	}


	public void setSignerKey(String signerKey) {
		this.signerKey = signerKey;
	}


	public ClientEndpointService getClientService() {
		return clientService;
	}


	public void setClientService(ClientEndpointService clientService) {
		this.clientService = clientService;
	}


	public GenericBatchBuilder getBatchBuilder() {
		return batchBuilder;
	}


	public void setBatchBuilder(GenericBatchBuilder batchBuilder) {
		this.batchBuilder = batchBuilder;
	}


	public Signer getSigner() {
		return signer;
	}


	public void setSigner(Signer signer) {
		this.signer = signer;
	}


	public abstract IAddressBuilder<ENTITY> getiAddressBuilder();

	public abstract GenericTransactionBuilder<ENTITY,PAYLOAD>  getTransactionBuilder();
}