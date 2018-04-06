/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.asset;

import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.client.BaseClientTest;
import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.core.service.asset.AssetAddressBuilder;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload.PayloadType;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.CreateAsset;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.UpdateAsset;

public class AssetPayloadClientTest extends BaseClientTest {
	
	Logger logger = Logger.getLogger(AssetPayloadClientTest.class.getName());

	@Override
	protected ClientService getClientService() {
		return new AssetPayloadClientService("asset", "1.0", null, getZMQAddress());
	}
	
	@Override
	protected String getZMQAddress() {
		return "tcp://localhost:4004";
	}
	
	@Test
	public void testCreateAsset() throws Exception {
		String assetName = "Computer";
		CreateAsset createAsset = CreateAsset.getDefaultInstance().newBuilder()
				.setName(assetName)
				.setType("Electronix")
				.setDetails("A Laptop used as test for Unit testing.")
				.setValue(1000)
				.build();
		AssetPayload payload = AssetPayload.newBuilder()
				.setCreateAsset(createAsset)
				.setPayloadType(PayloadType.CREATE_ASSET)
				.build();
		logger.info("Sending Payload as " + payload);
		String resposne = service.submitStateChange(payload);
		logger.info("State Change responsed from client Service : " + resposne);
		//92a8fd1ae7d38c35cf3201eba5d56d87554ab88bfc8cbf126858de412b78ce4b28c3b7
		//92a8fd1ae7d38c35cf3201eba5d56d87554ab88bfc8cbf126858de412b78ce4b28c3b7
		Asset asset = Asset.getDefaultInstance().newBuilder().setName(assetName).build();
		String address_mobile = new AssetAddressBuilder("asset","1.0").buildAddress(asset);
		System.out.println("Getting data for at address   :    "+address_mobile);
		ByteString response = zmqTemplate.getClientGetStateRequest(address_mobile);
		Asset assetAtAddress = Asset.parseFrom(response);
		System.out.println("Asset found as "+assetAtAddress);
		Assert.assertEquals(1000, assetAtAddress.getValue());	
		
	}
	
	
	@Test
	@Ignore
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
		String resposne = service.submitStateChange(payload);
		logger.info("Response from client Service : " + resposne);
	}

}
