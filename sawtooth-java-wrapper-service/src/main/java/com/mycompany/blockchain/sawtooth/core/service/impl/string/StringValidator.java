/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.impl.string;

import com.mycompany.blockchain.sawtooth.core.service.IDataValidator;

import sawtooth.sdk.processor.exceptions.InvalidTransactionException;

/**
 * 
 * A validator for string type
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class StringValidator implements IDataValidator<String> {

	@Override
	public boolean validate(String data) throws InvalidTransactionException {
		if (data == null || data.isEmpty()) {
			throw new InvalidTransactionException("Cannot store Null or Empty Data on the chain!");
		}
		return true;
	}

}
