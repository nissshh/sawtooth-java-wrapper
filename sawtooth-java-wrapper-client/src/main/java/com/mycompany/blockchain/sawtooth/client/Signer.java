package com.mycompany.blockchain.sawtooth.client;

import java.util.logging.Logger;

import org.bitcoinj.core.ECKey;

import sawtooth.sdk.client.Signing;

public class Signer {
	static Logger logger = Logger.getLogger(Signer.class.getName());

	/**
	 * The user / signer private key
	 */
	private String signerKey;

	/**
	 * ECKEy
	 */
	private ECKey privateKey = null;
	
	public Signer(String privateKey) {
		this.signerKey = privateKey;
	}

	/**
	 * Returns ECKey per the signer key configured;
	 * 
	 * @return
	 */
	public ECKey getSignerPrivateKey() {
		if (privateKey == null) {
			if (signerKey != null) {
				privateKey = Signing.readWif(signerKey); //:todo - check for this implementaion for siging to be workgni
				logger.info("Signer Key is set from supplied Private Key");
			} else {
				privateKey = Signing.generatePrivateKey(null); // new random privatekey
				logger.warning("Using Random Signer Key may not work at private network.");
			}
		}
		logger.info("Pub Key for signer is " + privateKey.getPublicKeyAsHex());
		return privateKey;
	}
}