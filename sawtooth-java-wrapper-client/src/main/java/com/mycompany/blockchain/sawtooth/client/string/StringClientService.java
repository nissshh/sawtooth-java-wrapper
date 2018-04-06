/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.string;

import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.impl.string.StringAddressBuilder;
import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;

/**
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class StringClientService extends ClientService<String,String> {
	public StringClientService(String txFamily, String txVersion, String signerKey, String address) {
		super(txFamily, txVersion, signerKey,address);
		iAddressBuilder = new StringAddressBuilder(txFamily, txVersion);
		transactionBuilder = new StringTransactionBuilder();
	}

	@Override
	public IAddressBuilder<String> getiAddressBuilder() {
		return iAddressBuilder;
	}

	@Override
	public GenericTransactionBuilder<String, String> getTransactionBuilder() {
		return transactionBuilder;
	}

	
}
