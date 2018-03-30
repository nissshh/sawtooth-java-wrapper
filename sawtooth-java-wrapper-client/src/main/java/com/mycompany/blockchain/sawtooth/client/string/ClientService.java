package com.mycompany.blockchain.sawtooth.client.string;

import java.util.logging.Logger;

import com.google.protobuf.ByteString;
import com.mashape.unirest.http.exceptions.UnirestException;

class StringClientService {

	private static Logger logger = Logger.getLogger(StringClientService.class.getName());
	private String txFamily;
	private String txVersion;
	private String signerKey;

	public StringClientService(String txFamily, String txVersion, String ignerKey) {
		this.txFamily = txFamily;
		this.txVersion = txVersion;
		this.signerKey = signerKey;
	}

	ClientEndpointService clientService;
	Signer signer;
	
	public void init() {
		
	}

	public void service() throws UnirestException {
		ByteString batchBytes = null;
		logger.info("Response for submission is : "+ clientService.submit(batchBytes));
	}

	public void destroy() {

	}
}