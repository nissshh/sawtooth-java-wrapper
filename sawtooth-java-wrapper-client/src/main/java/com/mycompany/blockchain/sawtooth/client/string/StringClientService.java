/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.string;

import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.impl.string.StringAddressBuilder;
import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;

/**
 * @author dev
 *
 */
public class StringClientService extends ClientService<String> {
	public StringClientService(String txFamily, String txVersion, String signerKey) {
		super(txFamily, txVersion, signerKey);
		iAddressBuilder = new StringAddressBuilder(txFamily, txVersion);
		transactionBuilder = new StringTransactionBuilder();
	}

	@Override
	public IAddressBuilder<String> getiAddressBuilder() {
		return iAddressBuilder;
	}

	@Override
	public GenericTransactionBuilder<String> getTransactionBuilder() {
		return null;
	}

	
}
