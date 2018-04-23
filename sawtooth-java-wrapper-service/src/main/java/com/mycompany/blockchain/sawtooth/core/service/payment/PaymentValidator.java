/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.payment;

import com.mycompany.blockchain.sawtooth.core.service.IDataValidator;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Payment;

import sawtooth.sdk.processor.exceptions.InvalidTransactionException;

/**
 * @author Sandip Nirmal
 *
 */
public class PaymentValidator implements IDataValidator<Payment> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mycompany.blockchain.sawtooth.core.service.IValidator#validate(java.lang. Object)
	 */
	@Override
	public boolean validate(Payment data) throws InvalidTransactionException {
		if (data.getId() == null || data.getId().isEmpty())
			throw new InvalidTransactionException("Payment Id cannot be empty or null.");
		return true;
	}

}
