/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.asset;

import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;

/**
 * @author dev
 *
 */
public class AssetAddressBuilder implements IAddressBuilder<Asset> {
	
	
	private String txFamilyName;
	
	private String txFamilyVer;

	public AssetAddressBuilder(String txFamilyName,String txFamilyVer) {
		this.txFamilyName = txFamilyName;
		this.txFamilyVer = txFamilyVer;
	}

	/* (non-Javadoc)
	 * @see com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder#getTransactionFamilyName()
	 */
	@Override
	public String getTransactionFamilyName() {
		return txFamilyName;
	}

	/* (non-Javadoc)
	 * @see com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder#getTransactionFamilyVersion()
	 */
	@Override
	public String getTransactionFamilyVersion() {
		return txFamilyVer;
	}

	/* (non-Javadoc)
	 * @see com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder#getEntityKey(java.lang.Object)
	 */
	@Override
	public String getEntityKey(Asset entity) {
		return entity.getName(); //uses name an identifier
	}

}
