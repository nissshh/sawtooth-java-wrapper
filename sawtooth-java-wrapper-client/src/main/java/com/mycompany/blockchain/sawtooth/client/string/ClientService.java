package com.mycompany.blockchain.sawtooth.client.string;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.protobuf.ByteString;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;

import sawtooth.sdk.protobuf.BatchList;
import sawtooth.sdk.protobuf.Transaction;

abstract class ClientService<ENTITY> {

	private static Logger logger = Logger.getLogger(ClientService.class.getName());
	
	private String txFamily;
	
	private String txVersion;
	
	private String signerKey;

	private ClientEndpointService clientService;
	
	private GenericBatchBuilder batchBuilder;
	
	private GenericTransactionBuilder<ENTITY> transactionBuilder;
	
	private Signer signer;
	
	protected IAddressBuilder<ENTITY> iAddressBuilder;

	public ClientService(String txFamily, String txVersion, String signerKey) {
		this.txFamily = txFamily;
		this.txVersion = txVersion;
		this.signerKey = signerKey;
	}

	
	public void init() {
		transactionBuilder = new GenericTransactionBuilder<>();
		signer = new Signer(signerKey);
		transactionBuilder.setSigner(signer);
		transactionBuilder.setiAddressBuilder(iAddressBuilder);
		batchBuilder= new GenericBatchBuilder();
		batchBuilder.setSigner(signer);
		clientService = new ClientEndpointService();
		
	}

	public String service(ENTITY payload) throws UnirestException {
		List<Transaction> transactions = new ArrayList<>();
		Transaction transaction = transactionBuilder.buildTransaction(payload);
		transactions.add(transaction);
		BatchList batch = batchBuilder.buildBatch(transactions);
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


	public abstract  void setiAddressBuilder(IAddressBuilder<ENTITY> iAddressBuilder);


	public void setTransactionBuilder(GenericTransactionBuilder<ENTITY> transactionBuilder) {
		this.transactionBuilder = transactionBuilder;
	}
}