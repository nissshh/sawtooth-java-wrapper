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
 * @author dev
 *
 */
public class IntKeyClientService extends ClientService<String> {

	
	public IntKeyClientService(String txFamily, String txVersion, String signerKey) {
		super(txFamily, txVersion, signerKey);
		iAddressBuilder = new IntKeyAddressBuilder(txFamily, txVersion); //set it
		transactionBuilder = new IntkeyTransactionBuilder();//set it
	}

	@Override
	public IAddressBuilder<String> getiAddressBuilder() {
		// TODO Auto-generated method stub
		return iAddressBuilder;
	}

	@Override
	public GenericTransactionBuilder<String> getTransactionBuilder() {
		// TODO Auto-generated method stub
		return transactionBuilder;
	}

	

}
