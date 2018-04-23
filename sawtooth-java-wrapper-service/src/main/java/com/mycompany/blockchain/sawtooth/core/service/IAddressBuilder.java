/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service;

import java.io.UnsupportedEncodingException;

import sawtooth.sdk.processor.Utils;

/**
 * 
 * Defines the address build mechanism and return as address based on 
 * entered data.
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public interface IAddressBuilder<ENTITY> {
	
	/**
	 * builds an address based on the entity data.
	 * 
	 * @param entity
	 * @return address for the entity
	 * @throws UnsupportedEncodingException 
	 */
	default String buildAddress(ENTITY entity) throws UnsupportedEncodingException {
		return Utils.hash512(getTransactionFamilyName().getBytes()).substring(0, 6)
				+ Utils.hash512(getEntityKey(entity).getBytes()).substring(0, 64);
	}

	default String getAddressPrefix() {
		return Utils.hash512(getTransactionFamilyName().getBytes()).substring(0, 6);
	}
	/**
	 * Provide the transaction family name as for prefix 
	 * 
	 * @return
	 */
	String getTransactionFamilyName();
	
	/**
	 * Proide the transaction family version
	 * @return
	 */
	
	String getTransactionFamilyVersion();

	/**
	 * Provides the way entity key is calculated, can be of certain type
	 * 
	 * E.g for Item it could be ItemName+Color+Shape as key
	 * @param entity
	 * @return a unmique identitfer for an entity to be then further used as address.
	 */
	String getEntityKey(ENTITY entity);

}
