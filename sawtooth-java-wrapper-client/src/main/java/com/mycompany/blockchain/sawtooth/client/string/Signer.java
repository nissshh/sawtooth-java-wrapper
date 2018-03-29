package com.mycompany.blockchain.sawtooth.client.string;

import java.util.logging.Logger;

import org.bitcoinj.core.ECKey;

import sawtooth.sdk.client.Signing;

class Signer {
	static Logger logger = Logger.getLogger(Signer.class.getName());

	/**
	 * The user / signer private key
	 */
	private String signerKey;

	public Signer(String privateKey) {
		this.signerKey = privateKey;
	}

	/**
	 * Returns ECKey per the signer key configured;
	 * 
	 * @return
	 */
	public ECKey getSignerPrivateKey() {
		ECKey privateKey = null;
		if (signerKey != null) {
			privateKey = Signing.readWif(signerKey);
			logger.info("Signer Key is set from supplied Private Key");
		} else {
			privateKey = Signing.generatePrivateKey(null); // new random privatekey
			logger.warning("Using Random Signer Key may not work at private network.");
		}
		return privateKey;
	}
}