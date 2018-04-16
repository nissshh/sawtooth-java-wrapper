package com.mycompany.blockchain.sawtooth.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mycompany.blockchain.sawtooth.client.wallet.WalletPayloadClientService;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.Deposit;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.PayloadType;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.TransferPayment;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.Withdraw;

import lombok.extern.slf4j.Slf4j;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse.Status;

@Slf4j
public class WalletService {

	@Autowired
	private WalletPayloadClientService service;

	public Status transferWallet(TransferPayment transferPayment) throws Exception {
		Withdraw withdrawWallet = Withdraw.newBuilder()
				.setCustomerId(transferPayment.getSourceCustomerId())
				.setAmount(transferPayment.getAmount()).build();
		SawtoothWalletPayload payloadDebit = SawtoothWalletPayload.newBuilder()
				.setPayloadType(PayloadType.WITHDRAW).setWithdraw(withdrawWallet).build();

		Deposit depositWallet = Deposit.newBuilder()
				.setCustomerId(transferPayment.getDestCustomerId())
				.setAmount(transferPayment.getAmount()).build();
		SawtoothWalletPayload payloadCredit = SawtoothWalletPayload.newBuilder()
				.setPayloadType(PayloadType.DEPOSIT).setDeposit(depositWallet).build();

		List<SawtoothWalletPayload> payloads = new ArrayList<>();
		payloads.add(payloadDebit);
		payloads.add(payloadCredit);

		log.info("Sending Payload as " + payloads);
		Status response = service.submitStateChangeMutipleTransactions(payloads);
		log.info("State Change responsed from client Service : " + response);
		return response;
	}

}
