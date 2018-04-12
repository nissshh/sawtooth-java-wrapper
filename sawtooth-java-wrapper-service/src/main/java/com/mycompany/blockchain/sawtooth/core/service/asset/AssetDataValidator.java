/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.asset;

import com.mycompany.blockchain.sawtooth.core.service.IDataValidator;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;

import sawtooth.sdk.processor.exceptions.InvalidTransactionException;

/**
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class AssetDataValidator implements IDataValidator<Asset> {

	/* (non-Javadoc)
	 * @see com.mycompany.blockchain.sawtooth.core.service.IValidator#validate(java.lang.Object)
	 */
	@Override
	public boolean validate(Asset data) throws InvalidTransactionException {
		if (data.getName() == null || data.getName().isEmpty()) throw new InvalidTransactionException("Asset[Name] cannot be empty or null.");
		if (data.getValue() <= 0 ) throw new InvalidTransactionException("Asset[Value] cannot be zero (0) or negative.");
		if (data.getPublicKey() == null || data.getPublicKey().isEmpty()) throw new InvalidTransactionException("Asset[Public Key] cannot be empty or null.");
		return true;
	}

}
