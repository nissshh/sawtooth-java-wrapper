package com.mycompany.blockchain.sawtooth.core.service;

import sawtooth.sdk.processor.exceptions.InvalidTransactionException;

/**
 * 
 * @author dev
 * 
 *
 */
public interface IValidator<ENTITY> {
	boolean validate(ENTITY data) throws InvalidTransactionException;
}
