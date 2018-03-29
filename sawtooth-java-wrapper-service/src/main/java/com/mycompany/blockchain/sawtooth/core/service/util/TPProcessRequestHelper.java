/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.util;

import sawtooth.sdk.protobuf.TpProcessRequest;

/**
 * 
 * Helper class for transaction Process Request
 * 
 * @author dev
 *
 */
public class TPProcessRequestHelper {

	/**
	 * Get the signer for the request, Get the user signing public key from header
	 * 
	 * @param transactionRequest
	 * @return Signer Key
	 */
	public static String getUserSigner(TpProcessRequest transactionRequest) {
		return transactionRequest.getHeader().getSignerPublicKey();
	}

	/**
	 * Extract the payload as utf8 str from the transaction, in request
	 * 
	 * @param transactionRequest
	 * @return
	 */
	public static String getPayload(TpProcessRequest transactionRequest) {
		return transactionRequest.getPayload().toStringUtf8();
	}
}
