/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.impl.string;


import sawtooth.sdk.processor.TransactionProcessor;

/**
 * Processor for StringHandler.
 * 
 * @author
 *
 */
public class StringProcessor {
	/**
	 * the method that runs a Thread with a TransactionProcessor in it.
	 */
	public static void main(String[] args) {
		TransactionProcessor transactionProcessor = new TransactionProcessor(args[0]);
		transactionProcessor.addHandler(new StringTranscationHandler("string", "1.0"));
		Thread thread = new Thread(transactionProcessor);
		thread.start();
	}
}
