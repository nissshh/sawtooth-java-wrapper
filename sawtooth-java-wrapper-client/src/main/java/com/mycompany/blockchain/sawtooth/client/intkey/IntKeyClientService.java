/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.intkey;

import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;
import com.mycompany.blockchain.sawtooth.client.IntkeyTransactionBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.imple.intkeyval.IntKeyAddressBuilder;

/**
 * 
 * A concreate client implementation for int key TP as client.
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class IntKeyClientService extends ClientService<String,String> {

	
	public IntKeyClientService(String txFamily, String txVersion, String signerKey, String address) {
		super(txFamily, txVersion, signerKey,address);
		iAddressBuilder = new IntKeyAddressBuilder(txFamily, txVersion); //set it
		transactionBuilder = new IntkeyTransactionBuilder();//set it
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
