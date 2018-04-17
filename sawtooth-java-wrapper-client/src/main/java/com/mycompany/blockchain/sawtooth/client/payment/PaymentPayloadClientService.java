/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.payment;

import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.payment.PaymentAddressBuilder;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Payment;
import com.mycompany.blockchain.sawtooth.loan.protobuf.PaymentPayload;

/**
 * A client for Loan TP
 * 
 * @author Sandip Nirmal
 *
 */
public class PaymentPayloadClientService extends ClientService<Payment, PaymentPayload> {

	public PaymentPayloadClientService(String txFamily, String txVersion, String signerKey,
			String address) {
		super(txFamily, txVersion, signerKey, address);
		iAddressBuilder = new PaymentAddressBuilder(txFamily, txVersion);
		transactionBuilder = new PaymentTransactionBuilder();
	}

	@Override
	public IAddressBuilder<Payment> getiAddressBuilder() {
		return iAddressBuilder;
	}

	@Override
	public GenericTransactionBuilder<Payment, PaymentPayload> getTransactionBuilder() {
		return transactionBuilder;
	}

}
