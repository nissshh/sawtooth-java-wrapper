package com.mycompany.blockchain.sawtooth.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.client.GenericBatchBuilder;
import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;
import com.mycompany.blockchain.sawtooth.client.TransactionHeaderDTO;
import com.mycompany.blockchain.sawtooth.client.loan.LoanPayloadClientService;
import com.mycompany.blockchain.sawtooth.client.payment.PaymentPayloadClientService;
import com.mycompany.blockchain.sawtooth.client.wallet.WalletPayloadClientService;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.LoanRePaymentPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.PayloadType;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Payment;
import com.mycompany.blockchain.sawtooth.loan.protobuf.PaymentPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.PaymentPayload.PaymentPayloadType;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.Deposit;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.Withdraw;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.Wallet;

import lombok.extern.slf4j.Slf4j;
import sawtooth.sdk.protobuf.BatchList;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse.Status;

/**
 * Loan Service
 * 
 * @author Sandip Nirmal
 *
 */

@Slf4j
@Service
public class LoanService {

	@Autowired
	private LoanPayloadClientService loanService;

	@Autowired
	private PaymentPayloadClientService paymentService;

	@Autowired
	private WalletPayloadClientService walletService;

	@Autowired
	private GenericBatchBuilder batchBuilder;

	/*
	 * @Autowired private WalletService walletService;
	 */

	public Status pay(LoanRePaymentPayload loanPaymentPayload) throws Exception {
		Random random = new Random();
		String id = String.valueOf(random.nextInt(999999));
		Payment payment = Payment.newBuilder().setId(id)
				.setAmount(loanPaymentPayload.getPayment().getAmount())
				.setFrom(loanPaymentPayload.getPayment().getFrom())
				.setTo(loanPaymentPayload.getPayment().getTo()).build();

		// Payment TP
		PaymentPayload paymentPayload = PaymentPayload.newBuilder()
				.setPaylodType(PaymentPayloadType.PAY).setPayment(payment).build();		
		TransactionHeaderDTO paymentDTO = getPaymentTransaction(paymentPayload);
		log.info("Sending Payment Payload as " + paymentPayload);

		// Wallet TP		
		Withdraw withdrawWallet = Withdraw.newBuilder()
				.setCustomerId(payment.getFrom())
				.setAmount(payment.getAmount()).build();
		SawtoothWalletPayload payloadDebit = SawtoothWalletPayload.newBuilder().setPayloadType(
				com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.PayloadType.WITHDRAW)
				.setWithdraw(withdrawWallet).build();
		TransactionHeaderDTO walletDebitPayload = getWalletTransaction(payloadDebit);
		log.info("Sending Wallet Debit Payload as " + walletDebitPayload);
		
		Deposit depositWallet = Deposit.newBuilder()
				.setCustomerId(payment.getTo())
				.setAmount(payment.getAmount()).build();
		SawtoothWalletPayload payloadCredit = SawtoothWalletPayload.newBuilder().setPayloadType(
				com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.PayloadType.DEPOSIT)
				.setDeposit(depositWallet).build();
		TransactionHeaderDTO walletCrebitPayload = getWalletTransaction(payloadCredit);
		log.info("Sending Wallet Crebit Payload as " + walletCrebitPayload);
		
		// Loan TP
		LoanRequestPayload loanRequestPayload = LoanRequestPayload.newBuilder()
				.setPayloadType(PayloadType.REPAYMENT).setLoanRepayment(loanPaymentPayload).build();
		TransactionHeaderDTO loanRepaymentPayload = getLoanTransaction(loanRequestPayload);
		log.info("Sending Loan Re-Payment Payload as " + loanPaymentPayload);
		
		List<TransactionHeaderDTO> transactionDTOs = new ArrayList<>();
		transactionDTOs.add(paymentDTO);
		transactionDTOs.add(walletDebitPayload);
		transactionDTOs.add(walletCrebitPayload);
		transactionDTOs.add(loanRepaymentPayload);
		
		BatchList batch = batchBuilder.buildBatch(transactionDTOs);
		ByteString batchBytes = batch.toByteString();
		Status response = loanService.getTemplate().submitBatch(batchBytes);
		log.info("Response for submission is : " + response.getNumber());
		return response;
		
	}

	public TransactionHeaderDTO getLoanTransaction(LoanRequestPayload loanRequestPayload)
			throws Exception {
		GenericTransactionBuilder<Loan, LoanRequestPayload> loanTransactionBuilder = loanService
				.getTransactionBuilder();
		return loanTransactionBuilder.buildTransaction(loanRequestPayload);
	}

	public TransactionHeaderDTO getPaymentTransaction(PaymentPayload paymentPayload)
			throws Exception {
		GenericTransactionBuilder<Payment, PaymentPayload> paymentTransactionBuilder = paymentService
				.getTransactionBuilder();
		return paymentTransactionBuilder.buildTransaction(paymentPayload);
	}

	public TransactionHeaderDTO getWalletTransaction(SawtoothWalletPayload walletPayload)
			throws Exception {
		GenericTransactionBuilder<Wallet, SawtoothWalletPayload> walletTrabsactionBuilder = walletService
				.getTransactionBuilder();
		return walletTrabsactionBuilder.buildTransaction(walletPayload);
	}

}
