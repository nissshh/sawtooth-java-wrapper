package com.mycompany.blockchain.sawtooth.core.service.payment;



import sawtooth.sdk.processor.TransactionProcessor;

public class PaymentProcessor {
	/**
	 * the method that runs a Thread with a TransactionProcessor in it.
	 */
	public static void main(String[] args) {

		TransactionProcessor transactionProcessor = new TransactionProcessor(args[0]);
		transactionProcessor.addHandler(new PaymentHandler());
		Thread thread = new Thread(transactionProcessor);
		thread.start();
	}

}
