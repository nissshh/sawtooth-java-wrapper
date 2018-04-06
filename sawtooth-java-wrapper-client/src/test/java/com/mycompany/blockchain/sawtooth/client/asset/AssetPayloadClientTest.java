/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.asset;

import java.util.logging.Logger;

import org.junit.Ignore;
import org.junit.Test;

import com.mycompany.blockchain.sawtooth.client.BaseClientTest;
import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload.PayloadType;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.CreateAsset;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.UpdateAsset;

public class AssetPayloadClientTest extends BaseClientTest {
	Logger logger = Logger.getLogger(AssetPayloadClientTest.class.getName());

	@Override
	protected ClientService getClientService() {
		return new AssetPayloadClientService("asset", "1.0", null);
	}

	@Test
	@Ignore
	public void testCreateAsset() throws Exception {

		CreateAsset createAsset = CreateAsset.getDefaultInstance().newBuilder()
				.setName("Mobile")
				.setType("Electronix")
				.setDetails("A Laptop used as test for Unit testing.")
				.setValue(1000)
				.build();
		AssetPayload payload = AssetPayload.newBuilder()
				.setCreateAsset(createAsset)
				.setPayloadType(PayloadType.CREATE_ASSET)
				.build();
		logger.info("Sending Payload as " + payload);
		String resposne = service.service(payload);
		logger.info("Response from client Service : " + resposne);
	}
	
	
	@Test
	//@Ignore
	public void testUpdateAsset() throws Exception {
		CreateAsset createAsset = CreateAsset.getDefaultInstance().newBuilder()
				.setName("Mobile")
				.setType("Electronix")
				.setDetails("A Mobile used as test for Unit testing.")
				.setValue(300)
				.build();
		UpdateAsset updateAsset = UpdateAsset.getDefaultInstance().newBuilder()
				.setName("Mobile")
				.setValue(500)
				.build();
		AssetPayload payload = AssetPayload.getDefaultInstance().newBuilder()
				.setUdpateAsset(updateAsset)
				.setPayloadType(PayloadType.UPDATE_ASSET)
				.build();
		logger.info("Sending Payload as " + payload);
		String resposne = service.service(payload);
		logger.info("Response from client Service : " + resposne);
	}


}
