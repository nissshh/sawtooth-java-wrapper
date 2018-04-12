package com.mycompany.blockchain.sawtooth.core.service;

import sawtooth.sdk.processor.exceptions.InvalidTransactionException;

/**
 * Validator class containg all validations to be done on the entity data.
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 * 
 *
 */
public interface IDataValidator<ENTITY> {
	/**
	 * Validates the data and returns the results
	 * @param data
	 * @return true in most cases for validated data.
	 * @throws InvalidTransactionException  in case of any invalid data is added, implementors to throguh the exception.
	 */
	boolean validate(ENTITY data) throws InvalidTransactionException;
}
