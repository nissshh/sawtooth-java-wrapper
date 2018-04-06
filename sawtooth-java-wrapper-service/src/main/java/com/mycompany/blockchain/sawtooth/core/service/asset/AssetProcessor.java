package com.mycompany.blockchain.sawtooth.core.service.asset;



import sawtooth.sdk.processor.TransactionProcessor;

public class AssetProcessor {
	/**
	 * the method that runs a Thread with a TransactionProcessor in it.
	 */
	public static void main(String[] args) {

		TransactionProcessor transactionProcessor = new TransactionProcessor(args[0]);
		transactionProcessor.addHandler(new AssetHandler());
		Thread thread = new Thread(transactionProcessor);
		thread.start();
	}

}
