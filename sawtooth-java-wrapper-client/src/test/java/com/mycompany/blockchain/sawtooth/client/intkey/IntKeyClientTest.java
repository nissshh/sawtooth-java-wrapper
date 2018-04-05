/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.intkey;

import java.util.logging.Logger;

import org.bitcoinj.core.ECKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sawtooth.sdk.client.Signing;

/**
 * @author dev
 *
 */
public class IntKeyClientTest {
	private static Logger logger = Logger.getLogger(IntKeyClientTest.class.getName());
	
	private IntKeyClientService service;

	@Before
	public void setup() {
		ECKey privateKey = Signing.generatePrivateKey(null); // new random privatekey
		String pvtKey = privateKey.getPrivateKeyAsHex();
		service = new IntKeyClientService("intkey", "1.0", null);  //sending null now.
		service.init();
	}

	@Test
	public void test() throws Exception {
		String payload = new String("inc nishant 100");
		System.out.println("Sending Payload as "+payload);
		String resposne = service.service(payload);
		logger.info("Response from client Service : "+resposne);
	}
	
	 

	@After
	public void teardown() {
		service.destroy();
	}
}
