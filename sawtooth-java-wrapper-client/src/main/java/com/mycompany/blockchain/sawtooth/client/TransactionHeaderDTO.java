/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client;

import sawtooth.sdk.protobuf.Transaction;

/**
 * 
 * A DTO Object to hold the transaction and associated header.
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class TransactionHeaderDTO {
	private Transaction transaction;
	
	private String headerSignature;

	
	
	public TransactionHeaderDTO(Transaction transaction, String headerSignature) {
		super();
		this.transaction = transaction;
		this.headerSignature = headerSignature;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public String getHeaderSignature() {
		return headerSignature;
	}

	public void setHeaderSignature(String headerSignature) {
		this.headerSignature = headerSignature;
	}
	
	
	
}
