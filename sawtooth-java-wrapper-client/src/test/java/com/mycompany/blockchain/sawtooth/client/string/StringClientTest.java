package com.mycompany.blockchain.sawtooth.client.string;

import java.util.logging.Logger;

import org.bitcoinj.core.ECKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mashape.unirest.http.exceptions.UnirestException;

import sawtooth.sdk.client.Signing;

/**
 * 
 */

/**
 * 
 * Sample application to test a client.
 * 
 * @author dev
 *
 */
public class StringClientTest {
	private static Logger logger = Logger.getLogger(StringClientTest.class.getName());
	StringClientService service;

	@Before
	public void setup() {
		ECKey privateKey = Signing.generatePrivateKey(null); // new random privatekey
		String pvtKey = privateKey.getPrivateKeyAsHex();
		service = new StringClientService("string", "1.0", null);  //sending null now.
		service.init();

	}

	@Test
	public void test() throws Exception {
		String payload = "test";
		String resposne = service.service(payload);
		logger.info("Response from client Service : "+resposne);
	}

	@After
	public void teardown() {
		service.destroy();
	}

}
