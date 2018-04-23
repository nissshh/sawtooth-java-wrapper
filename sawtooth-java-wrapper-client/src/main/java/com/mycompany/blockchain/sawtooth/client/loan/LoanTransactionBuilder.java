package com.mycompany.blockchain.sawtooth.client.loan;

import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.ApproveLoanRequest;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.CreateLoanRequest;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.LoanRePaymentPayload;

public class LoanTransactionBuilder extends GenericTransactionBuilder<Loan, LoanRequestPayload> {

	@Override
	protected byte[] getEncodedPayload(LoanRequestPayload payload) throws Exception {
		return payload.toByteArray(); // no encoding used.
	}

	@Override
	protected Loan getEntity(LoanRequestPayload payload) {
		if (payload.hasCreateLoanRequest()) {
			CreateLoanRequest createLoanRequest = payload.getCreateLoanRequest();
			return Loan.newBuilder().setAssetId(createLoanRequest.getAssetId())
					.setBorrowerId(createLoanRequest.getBorrowerId())
					.setLenderId(createLoanRequest.getLenderId())
					.setRequestedAmt(createLoanRequest.getRequestedAmt())
					.setRoi(createLoanRequest.getRoi()).build();
		} else if (payload.hasApproveLoanRequest()) {
			ApproveLoanRequest approvedLoanRequest = payload.getApproveLoanRequest();
			return Loan.newBuilder().setAssetId(approvedLoanRequest.getAssetId())
					.setBorrowerId(approvedLoanRequest.getBorrowerId())
					.setLenderId(approvedLoanRequest.getLenderId())
					.setApprovedAmt(approvedLoanRequest.getApprovedAmt())
					.setRoi(approvedLoanRequest.getRoi()).build();
		} else if (payload.hasLoanRepayment()) {
			LoanRePaymentPayload monthlyPayment = payload.getLoanRepayment();
			return Loan.newBuilder().setBorrowerId(monthlyPayment.getPayment().getFrom())
					.setLenderId(monthlyPayment.getPayment().getTo())
					.setAssetId(monthlyPayment.getAssetId()).build();
		} else {
			return null;
		}
	}
}
