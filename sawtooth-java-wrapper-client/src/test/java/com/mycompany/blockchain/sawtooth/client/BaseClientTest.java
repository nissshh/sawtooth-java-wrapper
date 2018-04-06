/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client;

import org.bitcoinj.core.ECKey;
import org.junit.After;
import org.junit.Before;

import sawtooth.sdk.client.Signing;

/**
 * @author dev
 *
 */
public abstract class BaseClientTest {
	
	protected ClientService service;
	
	protected ECKey privateKey;
	
	@Before
	public void setup() {
		privateKey = Signing.generatePrivateKey(null); // new random privatekey
		service =  getClientService();
		service.init();
	}
	
	@After
	public void teardown() {
		service.destroy();
	}

	protected abstract ClientService getClientService();
}
