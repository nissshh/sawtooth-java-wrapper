/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.wallet;

import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.wallet.WalletAddressBuilder;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.Wallet;

/**
 * A client for Asset TP
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class WalletPayloadClientService extends ClientService<Wallet, SawtoothWalletPayload> {

	public WalletPayloadClientService(String txFamily, String txVersion, String signerKey,
			String address) {
		super(txFamily, txVersion, signerKey, address);
		iAddressBuilder = new WalletAddressBuilder(txFamily, txVersion);
		transactionBuilder = new WalletTransactionBuilder();
	}

	@Override
	public IAddressBuilder<Wallet> getiAddressBuilder() {
		return iAddressBuilder;
	}

	@Override
	public GenericTransactionBuilder<Wallet, SawtoothWalletPayload> getTransactionBuilder() {
		return transactionBuilder;
	}

}
