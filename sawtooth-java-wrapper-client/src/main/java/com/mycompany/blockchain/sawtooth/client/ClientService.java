package com.mycompany.blockchain.sawtooth.client;

import java.util.logging.Logger;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;

import sawtooth.sdk.protobuf.BatchList;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse.Status;

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

	protected ClientZMQTemplate template;
	
	protected GenericBatchBuilder batchBuilder;
	
	protected GenericTransactionBuilder<ENTITY,PAYLOAD> transactionBuilder;
	
	protected Signer signer;
	
	protected IAddressBuilder<ENTITY> iAddressBuilder;

	private String address;

	public ClientService(String txFamily, String txVersion, String signerKey,String address) {
		this.txFamily = txFamily;
		this.txVersion = txVersion;
		this.signerKey = signerKey;
		this.address = address;
	}

	
	public void init() throws Exception {
		signer = new Signer(signerKey);
		transactionBuilder.setSigner(signer);
		transactionBuilder.setiAddressBuilder(iAddressBuilder);
		batchBuilder= new GenericBatchBuilder();
		batchBuilder.setSigner(signer);
		template = new ClientZMQTemplate(address);
		template.init();
	}


	public Status submitStateChange(PAYLOAD payload) throws Exception {
		TransactionHeaderDTO transaction = transactionBuilder.buildTransaction(payload);
		BatchList batch = batchBuilder.buildBatch(transaction);
		ByteString batchBytes = batch.toByteString();
		Status response = template.submitBatch(batchBytes);
		return response;		
	}
	
	public void requestState(PAYLOAD payload) {
		
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


	public ClientZMQTemplate getTemplate() {
		return template;
	}


	public void setTemplate(ClientZMQTemplate template) {
		this.template = template;
	}


	public void setTransactionBuilder(GenericTransactionBuilder<ENTITY, PAYLOAD> transactionBuilder) {
		this.transactionBuilder = transactionBuilder;
	}


	public void setiAddressBuilder(IAddressBuilder<ENTITY> iAddressBuilder) {
		this.iAddressBuilder = iAddressBuilder;
	}
}