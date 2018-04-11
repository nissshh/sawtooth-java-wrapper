/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.wallet;

import com.mycompany.blockchain.sawtooth.core.service.IValidator;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.Wallet;

import sawtooth.sdk.processor.exceptions.InvalidTransactionException;

/**
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class WalletValidator implements IValidator<Wallet> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mycompany.blockchain.sawtooth.core.service.IValidator#validate(java.lang.
	 * Object)
	 */
	@Override
	public boolean validate(Wallet data) throws InvalidTransactionException {
		if (data.getCustomerId() == null || data.getCustomerId().isEmpty())
			throw new InvalidTransactionException("Customer Id cannot be empty or null.");
		return true;
	}

}
