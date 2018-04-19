/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.intkey;

import java.util.logging.Logger;

import org.junit.Test;

import com.mycompany.blockchain.sawtooth.client.BaseClientTest;
import com.mycompany.blockchain.sawtooth.client.ClientService;

import lombok.extern.java.Log;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse.Status;

/**
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
@Log
public class IntKeyClientTest extends BaseClientTest{
	
	@Test
	public void test() throws Exception {
		String payload = new String("inc sonar 10");
		log.info("Sending Payload as "+payload);
		Status resposne = service.submitStateChange(payload);
		log.info("Response from client Service : "+resposne);
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
