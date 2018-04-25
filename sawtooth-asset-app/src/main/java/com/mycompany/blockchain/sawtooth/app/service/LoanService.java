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

	/**
	 * Method to repay against the loan
	 * @param loanPaymentPayload
	 * @return
	 * @throws Exception
	 */

	public Status pay(LoanRePaymentPayload loanPaymentPayload) throws Exception {
		
		Payment payment = Payment.newBuilder().setId(loanPaymentPayload.getPayment().getId())
				.setAmount(loanPaymentPayload.getPayment().getAmount())
				.setFrom(loanPaymentPayload.getPayment().getFrom())
				.setTo(loanPaymentPayload.getPayment().getTo()).build();

		// Payment TP
		PaymentPayload paymentPayload = PaymentPayload.newBuilder()
				.setPaylodType(PaymentPayloadType.PAY).setPayment(payment).build();		
		TransactionHeaderDTO paymentPayloadDTO = getPaymentTransaction(paymentPayload);
		log.info("Sending Payment Payload as " + paymentPayload);

		// Wallet TP		
		Withdraw withdrawWallet = Withdraw.newBuilder()
				.setCustomerId(payment.getFrom())
				.setAmount(payment.getAmount()).build();
		SawtoothWalletPayload payloadDebit = SawtoothWalletPayload.newBuilder().setPayloadType(
				com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.PayloadType.WITHDRAW)
				.setWithdraw(withdrawWallet).build();
		TransactionHeaderDTO walletDebitPayloadDTO = getWalletTransaction(payloadDebit);
		log.info("Sending Wallet Debit Payload as " + payloadDebit);
		
		Deposit depositWallet = Deposit.newBuilder()
				.setCustomerId(payment.getTo())
				.setAmount(payment.getAmount()).build();
		SawtoothWalletPayload payloadCredit = SawtoothWalletPayload.newBuilder().setPayloadType(
				com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.PayloadType.DEPOSIT)
				.setDeposit(depositWallet).build();
		TransactionHeaderDTO walletCrebitPayloadDTO = getWalletTransaction(payloadCredit);
		log.info("Sending Wallet Crebit Payload as " + payloadCredit);
		
		// Loan TP
		LoanRequestPayload loanRequestPayload = LoanRequestPayload.newBuilder()
				.setPayloadType(PayloadType.REPAYMENT).setLoanRepayment(loanPaymentPayload).build();
		TransactionHeaderDTO loanRepaymentPayloadDTO = getLoanTransaction(loanRequestPayload);
		log.info("Sending Loan Re-Payment Payload as " + loanPaymentPayload);
		
		List<TransactionHeaderDTO> transactionDTOs = new ArrayList<>();
		transactionDTOs.add(paymentPayloadDTO);
		transactionDTOs.add(walletDebitPayloadDTO);
		transactionDTOs.add(walletCrebitPayloadDTO);
		transactionDTOs.add(loanRepaymentPayloadDTO);
		
		BatchList batch = batchBuilder.buildBatch(transactionDTOs);
		ByteString batchBytes = batch.toByteString();
		Status response = loanService.getTemplate().submitBatch(batchBytes);
		log.info("Response for submission is : " + response.getNumber());
		return response;
		
	}
	
	/**
	 * Approve Loan Request. After Approval of Loan, the payment is made from Lender to Borrower
	 * @param loanPayload
	 * @return
	 * @throws Exception
	 */
	public Status approveLoan(LoanRequestPayload loanPayload) throws Exception {
		Random random = new Random();
		String id = String.valueOf(random.nextInt(999999));
		Payment payment = Payment.newBuilder().setId(id)
				.setAmount(loanPayload.getApproveLoanRequest().getApprovedAmt())
				.setFrom(loanPayload.getApproveLoanRequest().getLenderId())
				.setTo(loanPayload.getApproveLoanRequest().getBorrowerId()).build();
		
		// Payment TP
		PaymentPayload paymentPayload = PaymentPayload.newBuilder()
				.setPaylodType(PaymentPayloadType.PAY).setPayment(payment).build();
		TransactionHeaderDTO paymentPayloadDTO = getPaymentTransaction(paymentPayload);
		log.info("Sending Payment Payload as " + paymentPayload);

		// Wallet TP
		Withdraw withdrawWallet = Withdraw.newBuilder().setCustomerId(payment.getFrom())
				.setAmount(payment.getAmount()).build();
		SawtoothWalletPayload payloadDebit = SawtoothWalletPayload.newBuilder().setPayloadType(
				com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.PayloadType.WITHDRAW)
				.setWithdraw(withdrawWallet).build();
		TransactionHeaderDTO walletDebitPayloadDTO = getWalletTransaction(payloadDebit);
		log.info("Sending Wallet Debit Payload as " + payloadDebit);

		Deposit depositWallet = Deposit.newBuilder().setCustomerId(payment.getTo())
				.setAmount(payment.getAmount()).build();
		SawtoothWalletPayload payloadCredit = SawtoothWalletPayload.newBuilder().setPayloadType(
				com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.PayloadType.DEPOSIT)
				.setDeposit(depositWallet).build();
		TransactionHeaderDTO walletCrebitPayloadDTO = getWalletTransaction(payloadCredit);
		log.info("Sending Wallet Crebit Payload as " + payloadCredit);

		// Loan TP
		LoanRequestPayload loanApprovalPayload = LoanRequestPayload.newBuilder()
				.setPayloadType(PayloadType.APPROVE_LOAN).setApproveLoanRequest(loanPayload.getApproveLoanRequest()).build();
		TransactionHeaderDTO loanApprovalDTO = getLoanTransaction(loanApprovalPayload);
		log.info("Sending Loan Approval Payload as " + loanApprovalPayload);

		List<TransactionHeaderDTO> transactionDTOs = new ArrayList<>();
		transactionDTOs.add(paymentPayloadDTO);
		transactionDTOs.add(walletDebitPayloadDTO);
		transactionDTOs.add(walletCrebitPayloadDTO);
		transactionDTOs.add(loanApprovalDTO);

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
