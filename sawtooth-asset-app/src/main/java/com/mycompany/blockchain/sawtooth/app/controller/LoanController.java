package com.mycompany.blockchain.sawtooth.app.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.mycompany.blockchain.sawtooth.app.service.LoanService;
import com.mycompany.blockchain.sawtooth.app.vo.LoanVO;
import com.mycompany.blockchain.sawtooth.client.loan.LoanPayloadClientService;
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
	LoanService loanService;

	@RequestMapping(consumes = "application/json", method = RequestMethod.GET, path = "/loan")
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

	@RequestMapping(consumes = "application/json", method = RequestMethod.POST, path = "/loan/create")
	public @ResponseBody int createLoanRequest(@RequestBody LoanVO loanVo) throws Exception {
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
	public @ResponseBody int approveLoanRequest(@RequestBody LoanVO loanVo) throws Exception {
		ApproveLoanRequest approveWallet = ApproveLoanRequest.newBuilder()
				.setAssetId(loanVo.getAssetId()).setBorrowerId(loanVo.getBorrowerId())
				.setLenderId(loanVo.getLenderId()).setRoi(loanVo.getRoi())
				.setApprovedAmt(loanVo.getApprovedAmount()).setStatus(LoanStatus.APPROVED).build();
		LoanRequestPayload payload = LoanRequestPayload.newBuilder()
				.setPayloadType(PayloadType.APPROVE_LOAN).setApproveLoanRequest(approveWallet)
				.build();
		Status response = loanPayloadService.submitStateChange(payload);
		if (!response.equals(Status.OK) && !response.equals(Status.STATUS_UNSET)) {
			log.warn("Response for submission is : " + response.getNumber());
		}
		return response.getNumber();
	}

	@RequestMapping(consumes = "application/json", method = RequestMethod.POST, path = "/loan/payment")
	public @ResponseBody int loanPayment(@RequestBody LoanVO loanVo) throws Exception {
		Payment payment = Payment.newBuilder().setFrom(loanVo.getPayment().getFrom())
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
