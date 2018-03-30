package com.mycompany.blockchain.sawtooth.client.string;

import java.util.Random;
import java.util.logging.Logger;

import org.bitcoinj.core.ECKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.impl.string.StringAddressBuilder;

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
	public void test() throws UnirestException {
		String payload = "test";
		String resposne = service.service(payload);
		logger.info("Response from client Service : "+resposne);
	}

	@After
	public void teardown() {
		service.destroy();
	}

}


/**
 * Test implementation
 * @author dev
 *
 */
class StringClientService extends ClientService<String> {

	public StringClientService(String txFamily, String txVersion, String signerKey) {
		super(txFamily, txVersion, signerKey);
		iAddressBuilder = new StringAddressBuilder(txFamily, txVersion);
	}

	@Override
	public IAddressBuilder<String> getiAddressBuilder() {
		return iAddressBuilder;
	}

	@Override
	public void setiAddressBuilder(IAddressBuilder<String> iAddressBuilder) {
		this.iAddressBuilder = iAddressBuilder;
	}

}
