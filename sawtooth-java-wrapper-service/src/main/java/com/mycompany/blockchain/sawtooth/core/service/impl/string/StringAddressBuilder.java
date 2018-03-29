/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.impl.string;

import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;

/**
 * 
 * A simple string as key address builder
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class StringAddressBuilder implements IAddressBuilder<String> {

	private String txnFamilyName;

	private String txnFamilyVer;

	public StringAddressBuilder(String txnFamilyName, String txnFamilyVer) {
		this.txnFamilyName = txnFamilyName;
		this.txnFamilyVer = txnFamilyVer;
	}

	@Override
	public String getTransactionFamilyName() {
		return txnFamilyName;
	}

	@Override
	public String getEntityKey(String entity) {
		return entity.toString();
	}

	@Override
	public String getTransactionFamilyVersion() {
		return txnFamilyVer;
	}

}