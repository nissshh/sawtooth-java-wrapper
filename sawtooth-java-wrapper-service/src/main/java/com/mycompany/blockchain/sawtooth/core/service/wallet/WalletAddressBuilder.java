/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.wallet;

import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.Wallet;

/**
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class WalletAddressBuilder implements IAddressBuilder<Wallet> {

	private String txFamilyName;

	private String txFamilyVer;

	public WalletAddressBuilder(String txFamilyName, String txFamilyVer) {
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
	 * @see
	 * com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder#getEntityKey(
	 * java.lang.Object)
	 */
	@Override
	public String getEntityKey(Wallet entity) {
		return entity.getCustomerId(); // uses name an identifier
	}

}
