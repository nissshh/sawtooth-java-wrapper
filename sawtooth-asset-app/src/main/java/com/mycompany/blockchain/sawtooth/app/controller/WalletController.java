package com.mycompany.blockchain.sawtooth.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.mycompany.blockchain.sawtooth.app.service.WalletService;
import com.mycompany.blockchain.sawtooth.app.vo.wallet.WalletTransferVO;
import com.mycompany.blockchain.sawtooth.app.vo.wallet.WalletVO;
import com.mycompany.blockchain.sawtooth.client.wallet.WalletPayloadClientService;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.CreateWallet;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.Deposit;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.PayloadType;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.TransferPayment;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.Withdraw;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.Wallet;

import lombok.extern.slf4j.Slf4j;
import sawtooth.sdk.processor.exceptions.ValidatorConnectionError;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse.Status;
import sawtooth.sdk.protobuf.ClientStateListResponse.Entry;

/**
 * 
 * Provided rest controllers for wallet services.
 * 
 * @author Sandip Nirmal
 *
 */
@Controller
@Slf4j
public class WalletController {

	@Autowired
	WalletPayloadClientService walletPayloadService;

	@Autowired
	WalletService walletService;
	
	@Autowired
	private Gson gson;

	@RequestMapping(method = RequestMethod.GET, path = "/wallet")
	public @ResponseBody String getWallet(@RequestParam String customerId)
			throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError,
			UnsupportedEncodingException {
		Wallet entity = Wallet.newBuilder().setCustomerId(customerId).build();
		String address = walletPayloadService.getiAddressBuilder().buildAddress(entity);
		ByteString result = walletPayloadService.getTemplate().getClientGetStateRequest(address);
		String strin = JsonFormat.printToString(Wallet.parseFrom(result));
		log.info("JSON : ", strin);
		return strin;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/all_wallet")
	public @ResponseBody String getAllWallets()
			throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError,
			UnsupportedEncodingException {
		Wallet entity = Wallet.newBuilder().setCustomerId("").build();
		String address = walletPayloadService.getiAddressBuilder().buildAddress(entity);
		
		List<Entry> walletEntryList = walletPayloadService.getTemplate()
				.getClientListStateRequest(address.substring(0, 6));
		List<Wallet> allWallets = new ArrayList<>();
		walletEntryList.stream().forEach(entry -> {
			try {
				allWallets.add(Wallet.parseFrom(entry.getData()));
			} catch (InvalidProtocolBufferException e) {
				System.out.println(e);
			}
		});
		String result = gson.toJson(allWallets);
		log.info("JSON : ", result);
		return result;
	}


	@RequestMapping(consumes = "application/json", method = RequestMethod.POST, path = "/wallet")
	public @ResponseBody int putWallet(@RequestBody WalletVO walletVo) throws Exception {
		CreateWallet creteWallet = CreateWallet.newBuilder().setCustomerId(walletVo.getCustomerId())
				.setInitialBalance(walletVo.getBalance()).build();
		SawtoothWalletPayload payload = SawtoothWalletPayload.newBuilder()
				.setPayloadType(PayloadType.CREATE_WALLET).setCreateWallet(creteWallet).build();
		Status response = walletPayloadService.submitStateChange(payload);
		if (!response.equals(Status.OK) && !response.equals(Status.STATUS_UNSET)) {
			log.warn("Response for submission is : " + response.getNumber());
		}
		return response.getNumber();
	}

	@RequestMapping(consumes = "application/json", method = RequestMethod.POST, path = "/wallet/deposit")
	public @ResponseBody int deposit(@RequestBody WalletVO walletVo) throws Exception {
		Deposit depositWallet = Deposit.newBuilder().setCustomerId(walletVo.getCustomerId())
				.setAmount(walletVo.getBalance()).build();
		SawtoothWalletPayload payload = SawtoothWalletPayload.newBuilder()
				.setPayloadType(PayloadType.DEPOSIT).setDeposit(depositWallet).build();
		Status response = walletPayloadService.submitStateChange(payload);
		if (!response.equals(Status.OK) && !response.equals(Status.STATUS_UNSET)) {
			log.warn("Response for submission is : " + response.getNumber());
		}
		return response.getNumber();
	}

	@RequestMapping(consumes = "application/json", method = RequestMethod.POST, path = "/wallet/withdraw")
	public @ResponseBody int withdraw(@RequestBody WalletVO walletVo) throws Exception {
		Withdraw withdrawWallet = Withdraw.newBuilder().setCustomerId(walletVo.getCustomerId())
				.setAmount(walletVo.getBalance()).build();
		SawtoothWalletPayload payload = SawtoothWalletPayload.newBuilder()
				.setPayloadType(PayloadType.DEPOSIT).setWithdraw(withdrawWallet).build();
		Status response = walletPayloadService.submitStateChange(payload);
		if (!response.equals(Status.OK) && !response.equals(Status.STATUS_UNSET)) {
			log.warn("Response for submission is : " + response.getNumber());
		}
		return response.getNumber();
	}

	@RequestMapping(consumes = "application/json", method = RequestMethod.POST, path = "/wallet/transfer")
	public @ResponseBody int transferWallet(@RequestBody WalletTransferVO walletVo) throws Exception {
		TransferPayment transferPayment = TransferPayment.newBuilder()
				.setSourceCustomerId(walletVo.getCustomerId()).setAmount(walletVo.getBalance())
				.setDestCustomerId(walletVo.getDestCustomerId()).build();
		Status response = walletService.transferWallet(transferPayment);
		if (!response.equals(Status.OK) && !response.equals(Status.STATUS_UNSET)) {
			log.warn("Response for submission is : " + response.getNumber());
		}
		return response.getNumber();
	}
}
