/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.wallet;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.client.BaseClientTest;
import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.core.service.wallet.WalletAddressBuilder;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.CreateWallet;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.Deposit;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.PayloadType;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.Withdraw;
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
		String customerId = "1259";
		CreateWallet createWallet = CreateWallet.newBuilder().setCustomerId(customerId)
				.setInitialBalance(7000).build();

		SawtoothWalletPayload payload = SawtoothWalletPayload.newBuilder()
				.setCreateWallet(createWallet).build();

		logger.info("Sending Payload as " + payload);
		String resposne = service.submitStateChange(payload);
		logger.info("State Change responsed from client Service : " + resposne);
		Wallet wallet = Wallet.getDefaultInstance().newBuilder().setCustomerId(customerId).build();
		String address = new WalletAddressBuilder("wallet", "1.0").buildAddress(wallet);
		System.out.println("Getting data for at address   :    " + address);
		ByteString response = zmqTemplate.getClientGetStateRequest(address);
		Wallet walletAtAddress = Wallet.parseFrom(response);
		System.out.println("Asset found as " + walletAtAddress);
		// Assert.assertEquals(customerId, walletAtAddress.getCustomerId());

	}

	@Test
	public void testDepositWallet() throws Exception {
		String customerId = "1259";
		Deposit depositWallet = Deposit.newBuilder().setCustomerId(customerId).setAmount(5000)
				.build();

		SawtoothWalletPayload payload = SawtoothWalletPayload.newBuilder()
				.setPayloadType(PayloadType.DEPOSIT).setDeposit(depositWallet).build();

		logger.info("Sending Payload as " + payload);
		String resposne = service.submitStateChange(payload);
		logger.info("State Change responsed from client Service : " + resposne);
		Wallet wallet = Wallet.getDefaultInstance().newBuilder().setCustomerId(customerId).build();
		String address = new WalletAddressBuilder("wallet", "1.0").buildAddress(wallet);
		System.out.println("Getting data for at address   :    " + address);
		ByteString response = zmqTemplate.getClientGetStateRequest(address);
		Wallet walletAtAddress = Wallet.parseFrom(response);
		System.out.println("Asset found as " + walletAtAddress);
		Assert.assertEquals(customerId, walletAtAddress.getCustomerId());

	}

	@Test
	public void testWithdrawWallet() throws Exception {
		String customerId = "1259";
		Withdraw withdrawWallet = Withdraw.newBuilder().setCustomerId(customerId).setAmount(2000)
				.build();

		SawtoothWalletPayload payload = SawtoothWalletPayload.newBuilder()
				.setPayloadType(PayloadType.WITHDRAW).setWithdraw(withdrawWallet).build();

		logger.info("Sending Payload as " + payload);
		String resposne = service.submitStateChange(payload);
		logger.info("State Change responsed from client Service : " + resposne);
		Wallet wallet = Wallet.getDefaultInstance().newBuilder().setCustomerId(customerId).build();
		String address = new WalletAddressBuilder("wallet", "1.0").buildAddress(wallet);
		System.out.println("Getting data for at address   :    " + address);
		ByteString response = zmqTemplate.getClientGetStateRequest(address);
		Wallet walletAtAddress = Wallet.parseFrom(response);
		System.out.println("Asset found as " + walletAtAddress);
		Assert.assertEquals(customerId, walletAtAddress.getCustomerId());

	}

	@Test
	public void testTransferWallet() throws Exception {

		Withdraw withdrawWallet = Withdraw.newBuilder().setCustomerId("1259").setAmount(3000)
				.build();
		SawtoothWalletPayload payloadDebit = SawtoothWalletPayload.newBuilder()
				.setPayloadType(PayloadType.WITHDRAW).setWithdraw(withdrawWallet).build();

		Deposit depositWallet = Deposit.newBuilder().setCustomerId("1151").setAmount(3000).build();
		SawtoothWalletPayload payloadCredit = SawtoothWalletPayload.newBuilder()
				.setPayloadType(PayloadType.DEPOSIT).setDeposit(depositWallet).build();

		List<SawtoothWalletPayload> payloads = new ArrayList<>();
		payloads.add(payloadDebit);
		payloads.add(payloadCredit);

		logger.info("Sending Payload as " + payloads);
		String resposne = service.submitStateChangeMutiplePayloads(payloads);

		logger.info("State Change responsed from client Service : " + resposne);
		Wallet wallet = Wallet.getDefaultInstance().newBuilder().setCustomerId("1259").build();
		String address = new WalletAddressBuilder("wallet", "1.0").buildAddress(wallet);
		System.out.println("Getting data for at address   :    " + address);
		ByteString response = zmqTemplate.getClientGetStateRequest(address);
		Wallet walletAtAddress = Wallet.parseFrom(response);
		System.out.println("Asset found as " + walletAtAddress);
		// Assert.assertEquals(customerId, walletAtAddress.getCustomerId());

	}

}
