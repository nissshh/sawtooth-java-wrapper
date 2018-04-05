/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service;

import java.io.IOException;
import java.util.Collection;

import sawtooth.sdk.processor.State;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.processor.exceptions.InvalidTransactionException;

/**
 * 
 * Provides methods to get address data for a given entity key
 * 
 * Not: Key definition is client specific
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */


public interface IBaseDAO<KEY,ENTITY> {
	
	/**
	 * Get the current ledger entry from state for a given address.
	 * @param address
	 * @throws InvalidTransactionException 
	 * @throws InternalError 
	 */
	ENTITY getLedgerEntry(State stateInfo,KEY address) throws InternalError, InvalidTransactionException,IOException;
	
	
	/***
	 * Persists the data at the address.
	 * @param address
	 * @param data
	 * @return 
	 * @throws InvalidTransactionException 
	 * @throws InternalError 
	 */
	Collection<String> putLedgerEntry(State stateInfo,KEY address,ENTITY data) throws InternalError, InvalidTransactionException,IOException;

}
