/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.loan;

import com.mycompany.blockchain.sawtooth.core.service.IDataValidator;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;

import sawtooth.sdk.processor.exceptions.InvalidTransactionException;

/**
 * @author Sandip Nirmal
 *
 */
public class LoanValidator implements IDataValidator<Loan> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mycompany.blockchain.sawtooth.core.service.IValidator#validate(java.lang. Object)
	 */
	@Override
	public boolean validate(Loan data) throws InvalidTransactionException {
		if (data.getAssetId() == null || data.getAssetId().isEmpty())
			throw new InvalidTransactionException("Asset Id cannot be empty or null.");
		if (data.getBorrowerId() == null || data.getBorrowerId().isEmpty())
			throw new InvalidTransactionException("Borrower Id cannot be empty or null.");
		if (data.getLenderId() == null || data.getLenderId().isEmpty())
			throw new InvalidTransactionException("Lender Id cannot be empty or null.");
		return true;
	}

}
