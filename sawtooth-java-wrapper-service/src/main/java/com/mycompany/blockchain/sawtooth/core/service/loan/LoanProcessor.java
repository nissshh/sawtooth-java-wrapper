package com.mycompany.blockchain.sawtooth.core.service.loan;



import sawtooth.sdk.processor.TransactionProcessor;

public class LoanProcessor {
	/**
	 * the method that runs a Thread with a TransactionProcessor in it.
	 */
	public static void main(String[] args) {

		TransactionProcessor transactionProcessor = new TransactionProcessor(args[0]);
		transactionProcessor.addHandler(new LoanHandler());
		Thread thread = new Thread(transactionProcessor);
		thread.start();
	}

}
