package com.mycompany.blockchain.sawtooth.client.string;

import java.util.logging.Logger;

import org.bitcoinj.core.ECKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mashape.unirest.http.exceptions.UnirestException;

import sawtooth.sdk.client.Signing;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse.Status;

/**
 * 
 */

/**
 * 
 * Sample application to test a client.
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class StringClientTest {
	private static Logger logger = Logger.getLogger(StringClientTest.class.getName());
	StringClientService service;

	@Before
	public void setup() throws Exception {
		ECKey privateKey = Signing.generatePrivateKey(null); // new random privatekey
		String pvtKey = privateKey.getPrivateKeyAsHex();
		service = new StringClientService("string", "1.0", null, "XXXX");  //sending null now.
		service.init();

	}

	@Test
	public void test() throws Exception {
		String payload = "test";
		Status resposne = service.submitStateChange(payload);
		logger.info("Response from client Service : "+resposne);
	}

	@After
	public void teardown() {
		service.destroy();
	}

}
