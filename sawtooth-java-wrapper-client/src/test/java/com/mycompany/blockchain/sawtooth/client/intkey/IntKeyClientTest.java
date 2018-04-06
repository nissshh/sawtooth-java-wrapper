/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.intkey;

import java.util.logging.Logger;

import org.bitcoinj.core.ECKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mycompany.blockchain.sawtooth.client.BaseClientTest;
import com.mycompany.blockchain.sawtooth.client.ClientService;

import sawtooth.sdk.client.Signing;

/**
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class IntKeyClientTest extends BaseClientTest{
	
	private static Logger logger = Logger.getLogger(IntKeyClientTest.class.getName());
	
	@Test
	public void test() throws Exception {
		String payload = new String("inc sonar 10");
		System.out.println("Sending Payload as "+payload);
		String resposne = service.service(payload);
		logger.info("Response from client Service : "+resposne);
	}

	@Override
	protected ClientService getClientService() {
		return new IntKeyClientService("intkey", "1.0", null);  //sending null now.;
	}
}
