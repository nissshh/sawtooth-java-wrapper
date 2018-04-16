/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.loan;

import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;

/**
 * @author Sandip Nirmal
 *
 */
public class LoanAddressBuilder implements IAddressBuilder<Loan> {

	private String txFamilyName;

	private String txFamilyVer;

	public LoanAddressBuilder(String txFamilyName, String txFamilyVer) {
		this.txFamilyName = txFamilyName;
		this.txFamilyVer = txFamilyVer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder#
	 * getTransactionFamilyName()
	 */
	@Override
	public String getTransactionFamilyName() {
		return txFamilyName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder#
	 * getTransactionFamilyVersion()
	 */
	@Override
	public String getTransactionFamilyVersion() {
		return txFamilyVer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder#getEntityKey(
	 * java.lang.Object)
	 */
	@Override
	public String getEntityKey(Loan entity) {
		return entity.getAssetId() + entity.getBorrowerId() + entity.getLenderId();
	}

}
