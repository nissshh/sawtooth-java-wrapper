package com.mycompany.blockchain.sawtooth.app.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.blockchain.sawtooth.client.loan.LoanPayloadClientService;
import com.mycompany.blockchain.sawtooth.client.payment.PaymentPayloadClientService;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.LoanRePaymentPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.PayloadType;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Payment;
import com.mycompany.blockchain.sawtooth.loan.protobuf.PaymentPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.PaymentPayload.PaymentPayloadType;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.TransferPayment;

import lombok.extern.slf4j.Slf4j;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse.Status;

/**
 * Loan Service
 * @author Sandip Nirmal
 *
 */

@Slf4j
@Service
public class LoanService {

	@Autowired
	private LoanPayloadClientService service;

	@Autowired
	private PaymentPayloadClientService paymentService;
	
	@Autowired
	private WalletService walletService;
	
	

	public Status pay(LoanRePaymentPayload loanPaymentPayload) throws Exception {
		Random random = new Random();
		String id = String.valueOf(random.nextInt(999999));
		Payment payment = Payment.newBuilder()
				.setId(id)
				.setAmount(loanPaymentPayload.getPayment().getAmount())
				.setFrom(loanPaymentPayload.getPayment().getFrom())
				.setTo(loanPaymentPayload.getPayment().getTo()).build();

		//Payment TP
		PaymentPayload paymentPayload = PaymentPayload.newBuilder()
				.setPaylodType(PaymentPayloadType.PAY).setPayment(payment).build();
		log.info("Sending Payment Payload as " + paymentPayload);
		Status response = paymentService.submitStateChange(paymentPayload);
		log.info("State Change responsed from client Service : " + response);

		//Wallet TP
		TransferPayment transferPayment = TransferPayment.newBuilder()
				.setAmount(payment.getAmount()).setDestCustomerId(payment.getTo())
				.setSourceCustomerId(payment.getFrom()).build();
		walletService.transferWallet(transferPayment);
		
		//Loan TP
		LoanRequestPayload loanRequestPayload = LoanRequestPayload.newBuilder().setPayloadType(PayloadType.REPAYMENT)
				.setLoanRepayment(loanPaymentPayload).build();
		log.info("Sending Loan Payment Payload as " + loanPaymentPayload);
		response = service.submitStateChange(loanRequestPayload);
		log.info("State Change responsed from client Service : " + response);
		return response;
	}

}
