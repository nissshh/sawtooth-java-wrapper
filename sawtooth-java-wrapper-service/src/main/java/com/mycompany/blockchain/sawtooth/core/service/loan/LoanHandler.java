/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.loan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IBaseDAO;
import com.mycompany.blockchain.sawtooth.core.service.IDataValidator;
import com.mycompany.blockchain.sawtooth.core.service.IEntityConvertor;
import com.mycompany.blockchain.sawtooth.core.service.ITransactionHandler;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.ApproveLoanRequest;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.CreateLoanRequest;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload.LoanRePaymentPayload;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanStatus;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Payment;

import lombok.extern.java.Log;
import sawtooth.sdk.processor.State;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.processor.exceptions.InvalidTransactionException;
import sawtooth.sdk.protobuf.TpProcessRequest;

/**
 * 
 * @author Sandip Nirmal
 *
 */
@Log
public class LoanHandler implements ITransactionHandler<String, LoanRequestPayload> {

	public static final String TX_FAMILY_NAME = "loan";

	public static final String TX_FAMILY_VER = "1.0";

	public static final String CREDIT_ACTION = "CREDIT";

	public static final String DEBIT_ACTION = "DEBIT";

	// framwork usable classes
	private IAddressBuilder<Loan> loanAddressBuilder;

	private IEntityConvertor<ByteString, LoanRequestPayload, LoanPayloadParser> entityConvertor;

	private IDataValidator<Loan> loanValidator;

	private LoanPayloadParser loanPayloadParser;

	private IBaseDAO<String, Loan> loanDao;

	@Override
	public String transactionFamilyName() {
		return TX_FAMILY_NAME;
	}

	@Override
	public String getVersion() {
		return TX_FAMILY_VER;
	}

	public LoanHandler() {
		loanAddressBuilder = new LoanAddressBuilder(transactionFamilyName(), getVersion());
		loanPayloadParser = new LoanPayloadParser();
		entityConvertor = new LoanPayloadConvertor(loanPayloadParser);
		loanDao = new LoanDAO();
		loanValidator = new LoanValidator();
	}

	@Override
	public void apply(TpProcessRequest transactionRequest, State state)
			throws InvalidTransactionException, InternalError {
		log.info("Inside Apply got TP Request " + transactionRequest);
		try {
			LoanRequestPayload loanPayload = entityConvertor
					.convert(transactionRequest.getPayload());
			switch (loanPayload.getPayloadType()) {
			case CREATE_LOAN:
				createLoanRequest(transactionRequest, state, loanPayload.getCreateLoanRequest());
				break;
			case APPROVE_LOAN:
				approveLoanRequest(transactionRequest, state, loanPayload.getApproveLoanRequest());
				break;
			case REPAYMENT:
				mothlyPayment(transactionRequest, state, loanPayload.getLoanRepayment());
				break;

			default:
				throw new InvalidTransactionException("Invalid Operation...");
			}
		} catch (IOException e) {
			throw new InternalError(e.getMessage());
		}
		log.info("Finished Apply got TP Request");
	}

	/**
	 * Handles Action - Create Loan Request
	 * 
	 * @param transactionRequest
	 * @param state
	 * @param createLoanRequest
	 * @throws InvalidTransactionException
	 * @throws IOException
	 * @throws InternalError
	 */
	private void createLoanRequest(TpProcessRequest transactionRequest, State state,
			CreateLoanRequest createLoanRequest)
			throws InvalidTransactionException, InternalError, IOException {
		log.info("Inside createLoanRequest()");
		Loan loan = getLoan(transactionRequest, createLoanRequest); // build data.
		loanValidator.validate(loan); // validate data.

		String address = loanAddressBuilder.buildAddress(loan); // build address.
		Loan existingLoan = loanDao.getLedgerEntry(state, address);

		if (existingLoan != null && loan.getAssetId().equalsIgnoreCase(existingLoan.getAssetId())) {
			throw new InvalidTransactionException("Loan for asset id " + loan.getAssetId()
					+ " already exists for borrower " + loan.getBorrowerId());
		}

		loanDao.putLedgerEntry(state, address, loan); // persist data.
		log.info("Loan created for borrower id " + loan.getBorrowerId());
	}

	/**
	 * Handles Action - Deposit to Wallet
	 * 
	 * @param transactionRequest
	 * @param state
	 * @param createWallet
	 * @throws InvalidTransactionException
	 * @throws IOException
	 * @throws InternalError
	 */
	private void approveLoanRequest(TpProcessRequest transactionRequest, State state,
			ApproveLoanRequest approveLoanRequest)
			throws InvalidTransactionException, InternalError, IOException {
		log.info("Inside approveLoanRequest()");
		Loan loan = getLoan(transactionRequest, approveLoanRequest); // build data.
		loanValidator.validate(loan); // validate data.

		String address = loanAddressBuilder.buildAddress(loan); // build address.
		Loan existingLoan = loanDao.getLedgerEntry(state, address);

		if (existingLoan == null || existingLoan.getAssetId() == null
				|| existingLoan.getAssetId().isEmpty()) {
			throw new InvalidTransactionException("No Loan Request found for Asset Id "
					+ loan.getAssetId() + ". First create Loan Request.");
		}
		Loan approvedLoan = updateApprovedLoan(transactionRequest, approveLoanRequest,
				existingLoan);
		loanDao.putLedgerEntry(state, address, approvedLoan); // persist data.
		log.info("Loan approved for Asset Id " + loan.getAssetId());
	}

	/**
	 * Handles Action - Monthly Re Payments against Loan
	 * 
	 * @param transactionRequest
	 * @param state
	 * @param createWallet
	 * @throws InvalidTransactionException
	 * @throws IOException
	 * @throws InternalError
	 */
	private void mothlyPayment(TpProcessRequest transactionRequest, State state,
			LoanRePaymentPayload rePayment)
			throws InvalidTransactionException, InternalError, IOException {
		log.info("Inside mothlyPayment()");
		Loan loan = Loan.newBuilder().setBorrowerId(rePayment.getPayment().getFrom())
				.setLenderId(rePayment.getPayment().getTo())
				.setAssetId(rePayment.getAssetId()).build();
		loanValidator.validate(loan); // validate data.

		String address = loanAddressBuilder.buildAddress(loan); // build address.
		Loan existingLoan = loanDao.getLedgerEntry(state, address);

		if (!loanValidator.validate(existingLoan)) {
			throw new InvalidTransactionException("No Loan Request found for Asset Id "
					+ loan.getAssetId() + " Borrower Id " + loan.getBorrowerId() + " Lender Id"
					+ loan.getLenderId() + ". First create Loan Request.");
		}
		
		Loan updatedLoan = updateMontlyPayment(transactionRequest, rePayment.getPayment(), existingLoan);
		
		loanDao.putLedgerEntry(state, address, updatedLoan); // persist data.
		log.info("EMI for Loan paid. AsssetId:" + loan.getAssetId() + " LenderId:"
				+ loan.getLenderId() + " Borrower Id:" + loan.getLenderId());
	}

	/**
	 * create Loan from Create Loan request
	 * 
	 * @param transactionRequest
	 * @param createLoanRequest
	 * @return
	 */
	private Loan getLoan(TpProcessRequest transactionRequest, CreateLoanRequest createLoanRequest) {
		return Loan.newBuilder().setAssetId(createLoanRequest.getAssetId())
				.setBorrowerId(createLoanRequest.getBorrowerId())
				.setLenderId(createLoanRequest.getLenderId())
				.setRequestedAmt(createLoanRequest.getRequestedAmt())
				.setRoi(createLoanRequest.getRoi()).setStatus(LoanStatus.REQUESTED).build();
	}

	/**
	 * create Loan from Approve Loan Request
	 * 
	 * @param transactionRequest
	 * @param approvedLoanRequest
	 * @return
	 */
	private Loan getLoan(TpProcessRequest transactionRequest,
			ApproveLoanRequest approvedLoanRequest) {
		return Loan.newBuilder().setAssetId(approvedLoanRequest.getAssetId())
				.setBorrowerId(approvedLoanRequest.getBorrowerId())
				.setLenderId(approvedLoanRequest.getLenderId())
				.setApprovedAmt(approvedLoanRequest.getApprovedAmt())
				.setRoi(approvedLoanRequest.getRoi()).build();
	}

	/**
	 * Update Loan from Approve Loan Request
	 * 
	 * @param transactionRequest
	 * @param loan
	 * @return
	 */
	private Loan updateApprovedLoan(TpProcessRequest transactionRequest,
			ApproveLoanRequest approveLoanRequest, Loan loan) {
		return Loan.newBuilder().setAssetId(loan.getAssetId()).setBorrowerId(loan.getBorrowerId())
				.setLenderId(loan.getLenderId()).setRequestedAmt(loan.getRequestedAmt())
				.setApprovedAmt(approveLoanRequest.getApprovedAmt())
				.setBalance(approveLoanRequest.getApprovedAmt())
				.setRoi(approveLoanRequest.getRoi()).setStatus(approveLoanRequest.getStatus())
				.build();
	}
	
	/**
	 * Update Monthly Re Payment in the Loan
	 * @param transactionRequest
	 * @param payment
	 * @param existingLoan
	 * @return
	 */
	private Loan updateMontlyPayment(TpProcessRequest transactionRequest,
			Payment payment, Loan existingLoan) {
		List<Payment> paymentsList = new ArrayList<Payment>();
		paymentsList.addAll(existingLoan.getPaymentsList());
		paymentsList.add(payment);
		
		//int interest = (int)(((existingLoan.getBalance() * existingLoan.getRoi())/100) / 12);
		int balance = existingLoan.getBalance() - (payment.getAmount());
		LoanStatus status = existingLoan.getStatus();
		if (balance <= 0) {
			status = LoanStatus.CLOSED;
		}
		 return Loan.newBuilder().setAssetId(existingLoan.getAssetId()).setBorrowerId(existingLoan.getBorrowerId())
				.setLenderId(existingLoan.getLenderId()).setRequestedAmt(existingLoan.getRequestedAmt())
				.setApprovedAmt(existingLoan.getApprovedAmt()).setId(existingLoan.getId()).setBalance(balance < 0 ? 0 : balance)
				.setRoi(existingLoan.getRoi()).setStatus(status).addAllPayments(paymentsList)
				.build();
	}

	@Override
	public IAddressBuilder<LoanRequestPayload> getAddressBuilder() {
		return null;
	}

	@Override
	public IBaseDAO<String, LoanRequestPayload> getBaseDAO() {
		return null;
	}

}
