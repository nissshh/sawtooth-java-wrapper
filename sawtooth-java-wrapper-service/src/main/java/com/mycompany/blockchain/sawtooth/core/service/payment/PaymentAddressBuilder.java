/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.payment;

import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Payment;

/**
 * @author Sandip Nirmal
 *
 */
public class PaymentAddressBuilder implements IAddressBuilder<Payment> {

	private String txFamilyName;

	private String txFamilyVer;

	public PaymentAddressBuilder(String txFamilyName, String txFamilyVer) {
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
	public String getEntityKey(Payment entity) {
		return entity.getId();
	}

}
