/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.asset;

import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.client.BaseClientTest;
import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.client.wallet.WalletPayloadClientService;
import com.mycompany.blockchain.sawtooth.core.service.wallet.WalletAddressBuilder;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.CreateWallet;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.Wallet;

public class WalletPayloadClientTest extends BaseClientTest {

	Logger logger = Logger.getLogger(WalletPayloadClientTest.class.getName());

	@Override
	protected ClientService getClientService() {
		return new WalletPayloadClientService("wallet", "1.0", null, getZMQAddress());
	}

	@Override
	protected String getZMQAddress() {
		return "tcp://localhost:4004";
	}

	@Test
	public void testCreateWallet() throws Exception {
		String assetName = "Computer";

		CreateWallet createWallet = CreateWallet.newBuilder().setCustomerId("001")
				.setInitialBalance(5000).build();

		SawtoothWalletPayload payload = SawtoothWalletPayload.newBuilder()
				.setCreateWallet(createWallet).build();

		logger.info("Sending Payload as " + payload);
		String resposne = service.submitStateChange(payload);
		logger.info("State Change responsed from client Service : " + resposne);
		Wallet wallet = Wallet.getDefaultInstance().newBuilder().setCustomerId("001").build();
		String address = new WalletAddressBuilder("wallet", "1.0").buildAddress(wallet);
		System.out.println("Getting data for at address   :    " + address);
		ByteString response = zmqTemplate.getClientGetStateRequest(address);
		Wallet walletAtAddress = Wallet.parseFrom(response);
		System.out.println("Asset found as " + walletAtAddress);
		Assert.assertEquals("001", walletAtAddress.getCustomerId());

	}

	/*@Test
	@Ignore
	public void testUpdateAsset() throws Exception {
		CreateAsset createAsset = CreateAsset.getDefaultInstance().newBuilder().setName("Mobile")
				.setType("Electronix").setDetails("A Mobile used as test for Unit testing.")
				.setValue(300).build();
		UpdateAsset updateAsset = UpdateAsset.getDefaultInstance().newBuilder().setName("Mobile")
				.setValue(500).build();
		AssetPayload payload = AssetPayload.getDefaultInstance().newBuilder()
				.setUdpateAsset(updateAsset).setPayloadType(PayloadType.UPDATE_ASSET).build();
		logger.info("Sending Payload as " + payload);
		String resposne = service.submitStateChange(payload);
		logger.info("Response from client Service : " + resposne);
	}*/

}
