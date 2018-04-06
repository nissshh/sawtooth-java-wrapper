package com.mycompany.blockchain.sawtooth.core.service;

import sawtooth.sdk.processor.exceptions.InvalidTransactionException;

/**
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 * 
 *
 */
public interface IValidator<ENTITY> {
	boolean validate(ENTITY data) throws InvalidTransactionException;
}
