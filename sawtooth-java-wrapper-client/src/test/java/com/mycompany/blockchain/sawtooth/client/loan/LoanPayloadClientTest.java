/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.loan;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.client.BaseClientTest;
import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.core.service.loan.LoanAddressBuilder;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.ApproveLoanRequest;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.CreateLoanRequest;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.LoanPaymentPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.PayloadType;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Payment;

import sawtooth.sdk.processor.exceptions.ValidatorConnectionError;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse.Status;

public class LoanPayloadClientTest extends BaseClientTest {

	Logger logger = Logger.getLogger(LoanPayloadClientTest.class.getName());

	String assetId = "A001", borrowerId = "B001", lenderId = "L001";

	@Override
	protected ClientService getClientService() {
		return new LoanPayloadClientService("loan", "1.0", null, getZMQAddress());
	}

	@Override
	protected String getZMQAddress() {
		return "tcp://localhost:4004";
	}

	@Test
	public void testCreateLoan() throws Exception {

		CreateLoanRequest cerateLoan = CreateLoanRequest.getDefaultInstance().newBuilder()
				.setAssetId(assetId).setBorrowerId(borrowerId).setLenderId(lenderId)
				.setRequestedAmt(10000).setRoi(7.5f).build();
		LoanRequestPayload payload = LoanRequestPayload.newBuilder()
				.setCreateLoanRequest(cerateLoan).setPayloadType(PayloadType.CREATE_LOAN).build();

		logger.info("Sending Payload as " + payload);
		Status resposne = service.submitStateChange(payload);
		logger.info("State Change responsed from client Service : " + resposne);
	}

	@Test
	public void testApproveLoan() throws Exception {

		ApproveLoanRequest approveLoan = ApproveLoanRequest.getDefaultInstance().newBuilder()
				.setAssetId(assetId).setBorrowerId(borrowerId).setLenderId(lenderId)
				.setApprovedAmt(10000).setRoi(8f).build();
		LoanRequestPayload payload = LoanRequestPayload.newBuilder()
				.setApproveLoanRequest(approveLoan)
				.setPayloadType(PayloadType.APPROVE_LOAN)
				.build();

		logger.info("Sending Payload as " + payload);
		Status resposne = service.submitStateChange(payload);
		logger.info("State Change responsed from client Service : " + resposne);
	}
	
	@Test
	public void testPayLoanEmi() throws Exception {

		Payment payment = Payment.getDefaultInstance().newBuilder().setFrom(borrowerId)
				.setTo(lenderId).setAmount(1000).setId("P003").build();

		// Payment Transaction Processor
		//PaymentPayload paymentPayload = PaymentPayload.newBuilder()
		//		.setPaylodType(PaymentPayloadType.PAY).setPayment(payment).build();
		//logger.info("Sending Payment Payload as " + paymentPayload);
		//PaymentPayloadClientService paymentService = new PaymentPayloadClientService("payment", "1.0", null, getZMQAddress());
		//Status resposne = paymentService.submitStateChange(paymentPayload);
		//logger.info("State Change responsed from client Service : " + resposne);

		// Loan Transaction Processor
		LoanPaymentPayload loanPaymentPayload = LoanPaymentPayload.newBuilder().setPayment(payment)
				.setAssetId(assetId).build();
		LoanRequestPayload payload = LoanRequestPayload.newBuilder()
				.setMonthlyPayment(loanPaymentPayload).setPayloadType(PayloadType.MONTHLY_PAYMENT)
				.build();
		logger.info("Sending Loan Payment Payload as " + payload);
		Status resposne = service.submitStateChange(payload);
		logger.info("State Change responsed from client Service : " + resposne);
	}

	@Test
	public void testReadData() throws UnsupportedEncodingException, InvalidProtocolBufferException,
			InterruptedException, ValidatorConnectionError {
		Loan loan = Loan.getDefaultInstance().newBuilder().setAssetId(assetId)
				.setBorrowerId(borrowerId).setLenderId(lenderId).build();
		String address = new LoanAddressBuilder("loan", "1.0").buildAddress(loan);
		System.out.println("Getting data for at address   :    " + address);
		ByteString response = zmqTemplate.getClientGetStateRequest(address);
		Loan loanAtAddress = Loan.parseFrom(response);
		System.out.println("Loan found as " + loanAtAddress);
		Assert.assertEquals(assetId, loanAtAddress.getAssetId());
	}

}
