/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.intkey;

import java.util.logging.Logger;

import org.junit.Test;

import com.mycompany.blockchain.sawtooth.client.BaseClientTest;
import com.mycompany.blockchain.sawtooth.client.ClientService;

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
		String resposne = service.submitStateChange(payload);
		logger.info("Response from client Service : "+resposne);
	}

	@Override
	protected ClientService getClientService() {
		return new IntKeyClientService("intkey", "1.0", null, getZMQAddress());  //sending null now.;
	}
	
	@Override
	protected String getZMQAddress() {
		return "tcp://localhost:4004";
	}
}
