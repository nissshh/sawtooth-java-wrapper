/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.impl.string;

import com.mycompany.blockchain.sawtooth.core.service.IValidator;

import sawtooth.sdk.processor.exceptions.InvalidTransactionException;

/**
 * 
 * A validator for string type
 * 
 * @author dev
 *
 */
public class StringValidator implements IValidator<String> {

	@Override
	public boolean validate(String data) throws InvalidTransactionException {
		if (data == null || data.isEmpty()) {
			throw new InvalidTransactionException("Cannot store Null or Empty Data on the chain!");
		}
		return true;
	}

}
