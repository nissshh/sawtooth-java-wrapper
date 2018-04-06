/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.asset;

import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.asset.AssetAddressBuilder;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload;

/**
 * A client for Asset TP
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class AssetPayloadClientService extends ClientService<Asset,AssetPayload> {

	
	public AssetPayloadClientService(String txFamily, String txVersion, String signerKey) {
		super(txFamily, txVersion, signerKey);
		iAddressBuilder = new AssetAddressBuilder(txFamily, txVersion);// the address builder is share b/w client and server.
		transactionBuilder = new AssetTransactionBuilder();
	}

	@Override
	public IAddressBuilder<Asset> getiAddressBuilder() {
		return iAddressBuilder;
	}

	@Override
	public GenericTransactionBuilder<Asset, AssetPayload> getTransactionBuilder() {
		return transactionBuilder;
	}


}
