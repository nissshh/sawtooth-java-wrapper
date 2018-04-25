package com.mycompany.blockchain.sawtooth.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.mycompany.blockchain.sawtooth.app.service.LoanService;
import com.mycompany.blockchain.sawtooth.app.vo.loan.ApproveLoanVO;
import com.mycompany.blockchain.sawtooth.app.vo.loan.CreateLoanVO;
import com.mycompany.blockchain.sawtooth.app.vo.loan.LoanPaymentVO;
import com.mycompany.blockchain.sawtooth.client.loan.LoanPayloadClientService;
import com.mycompany.blockchain.sawtooth.client.payment.PaymentPayloadClientService;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.ApproveLoanRequest;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.CreateLoanRequest;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.LoanRePaymentPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.PayloadType;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanStatus;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Payment;

import lombok.extern.slf4j.Slf4j;
import sawtooth.sdk.processor.exceptions.ValidatorConnectionError;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse.Status;
import sawtooth.sdk.protobuf.ClientStateListResponse.Entry;

/**
 * 
 * Provided rest controllers for Loan services.
 * 
 * @author Sandip Nirmal
 *
 */
@Controller
@Slf4j
public class LoanController {

	@Autowired
	LoanPayloadClientService loanPayloadService;
	
	@Autowired
	PaymentPayloadClientService paymentService;

	@Autowired
	LoanService loanService;
	
	@Autowired
	private Gson gson;

	@RequestMapping(method = RequestMethod.GET, path = "/loan")
	public @ResponseBody String getLoan(@RequestParam String assetId, String lenderId, String borrowerId)
			throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError,
			UnsupportedEncodingException {
		Loan entity = Loan.newBuilder().setAssetId(assetId)
				.setBorrowerId(borrowerId).setLenderId(lenderId).build();
		String address = loanPayloadService.getiAddressBuilder().buildAddress(entity);
		ByteString result = loanPayloadService.getTemplate().getClientGetStateRequest(address);
		String strin = JsonFormat.printToString(Loan.parseFrom(result));
		log.info("JSON : ", strin);
		return strin;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/all_loan")
	public @ResponseBody String getAllLoans() throws InvalidProtocolBufferException,
			InterruptedException, ValidatorConnectionError, UnsupportedEncodingException {
		Loan entity = Loan.newBuilder().setAssetId("").setBorrowerId("").setLenderId("").build();
		String address = loanPayloadService.getiAddressBuilder().buildAddress(entity);

		List<Entry> loanEntryList = loanPayloadService.getTemplate()
				.getClientListStateRequest(address.substring(0, 6));
		List<Loan> allLoans = new ArrayList<>();
		loanEntryList.stream().forEach(entry -> {
			try {
				allLoans.add(Loan.parseFrom(entry.getData()));
			} catch (InvalidProtocolBufferException e) {
				System.out.println(e);
			}
		});
		String result = gson.toJson(allLoans);
		log.info("JSON : ", result);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/payment")
	public @ResponseBody String getPayment(@RequestParam String paymentId)
			throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError,
			UnsupportedEncodingException {
		Payment payment = Payment.newBuilder().setId(paymentId).build();
		String address = paymentService.getiAddressBuilder().buildAddress(payment);
		ByteString result = paymentService.getTemplate().getClientGetStateRequest(address);
		String strin = JsonFormat.printToString(Payment.parseFrom(result));
		log.info("JSON : ", strin);
		return strin;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/all_payment")
	public @ResponseBody String getAllPayments() throws InvalidProtocolBufferException,
			InterruptedException, ValidatorConnectionError, UnsupportedEncodingException {
		Payment payment = Payment.newBuilder().setId("").build();
		String address = paymentService.getiAddressBuilder().buildAddress(payment);

		List<Entry> paymentEntryList = paymentService.getTemplate()
				.getClientListStateRequest(address.substring(0, 6));
		List<Payment> allPayments = new ArrayList<>();
		paymentEntryList.stream().forEach(entry -> {
			try {
				allPayments.add(Payment.parseFrom(entry.getData()));
			} catch (InvalidProtocolBufferException e) {
				System.out.println(e);
			}
		});
		String result = gson.toJson(allPayments);
		log.info("JSON : ", result);
		return result;
	}
	
	@RequestMapping(consumes = "application/json", method = RequestMethod.POST, path = "/loan/create")
	public @ResponseBody int createLoanRequest(@RequestBody CreateLoanVO loanVo) throws Exception {
		CreateLoanRequest creteWallet = CreateLoanRequest.newBuilder()
				.setAssetId(loanVo.getAssetId()).setBorrowerId(loanVo.getBorrowerId())
				.setLenderId(loanVo.getLenderId()).setRoi(loanVo.getRoi())
				.setRequestedAmt(loanVo.getRequestedAmount()).setStatus(LoanStatus.REQUESTED).build();
		LoanRequestPayload payload = LoanRequestPayload.newBuilder()
				.setPayloadType(PayloadType.CREATE_LOAN).setCreateLoanRequest(creteWallet).build();
		Status response = loanPayloadService.submitStateChange(payload);
		if (!response.equals(Status.OK) && !response.equals(Status.STATUS_UNSET)) {
			log.warn("Response for submission is : " + response.getNumber());
		}
		return response.getNumber();
	}

	@RequestMapping(consumes = "application/json", method = RequestMethod.POST, path = "/loan/approve")
	public @ResponseBody int approveLoanRequest(@RequestBody ApproveLoanVO loanVo) throws Exception {
		ApproveLoanRequest approveWallet = ApproveLoanRequest.newBuilder()
				.setAssetId(loanVo.getAssetId()).setBorrowerId(loanVo.getBorrowerId())
				.setLenderId(loanVo.getLenderId()).setRoi(loanVo.getRoi())
				.setApprovedAmt(loanVo.getApprovedAmount()).setStatus(LoanStatus.APPROVED).build();
		LoanRequestPayload payload = LoanRequestPayload.newBuilder()
				.setPayloadType(PayloadType.APPROVE_LOAN).setApproveLoanRequest(approveWallet)
				.build();
		Status response = loanService.approveLoan(payload);
		if (!response.equals(Status.OK) && !response.equals(Status.STATUS_UNSET)) {
			log.warn("Response for submission is : " + response.getNumber());
		}
		return response.getNumber();
	}

	@RequestMapping(consumes = "application/json", method = RequestMethod.POST, path = "/loan/payment")
	public @ResponseBody int loanPayment(@RequestBody LoanPaymentVO loanVo) throws Exception {
		Random random = new Random();
		String id = String.valueOf(random.nextInt(999999));
		Payment payment = Payment.newBuilder().setId(id).setFrom(loanVo.getPayment().getFrom())
				.setTo(loanVo.getPayment().getTo()).setAmount(loanVo.getPayment().getAmount())
				.build();
		
		LoanRePaymentPayload loanPaymentPayload = LoanRePaymentPayload.newBuilder().setAssetId(loanVo.getAssetId()).setPayment(payment).build();
				
		Status response = loanService.pay(loanPaymentPayload);
		if (!response.equals(Status.OK) && !response.equals(Status.STATUS_UNSET)) {
			log.warn("Response for submission is : " + response.getNumber());
		}
		return response.getNumber();
	}
}
