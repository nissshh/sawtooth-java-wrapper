/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.loan;

import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.loan.LoanAddressBuilder;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload;

/**
 * A client for Loan TP
 * 
 * @author Sandip Nirmal
 *
 */
public class LoanPayloadClientService extends ClientService<Loan, LoanRequestPayload> {

	public LoanPayloadClientService(String txFamily, String txVersion, String signerKey,
			String address) {
		super(txFamily, txVersion, signerKey, address);
		iAddressBuilder = new LoanAddressBuilder(txFamily, txVersion);
		transactionBuilder = new LoanTransactionBuilder();
	}

	@Override
	public IAddressBuilder<Loan> getiAddressBuilder() {
		return iAddressBuilder;
	}

	@Override
	public GenericTransactionBuilder<Loan, LoanRequestPayload> getTransactionBuilder() {
		return transactionBuilder;
	}

}
