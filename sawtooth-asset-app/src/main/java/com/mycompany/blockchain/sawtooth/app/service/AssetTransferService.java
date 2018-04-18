/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.client.asset.AssetPayloadClientService;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.UpdateAsset;

/**
 * 
 * A service class to transfer asset.
 * @author dev
 *
 */
@Service
public class AssetTransferService {
	
	@Autowired
	AssetPayloadClientService assetPayloadClientService;

	
	/**
	 * Transfer the Asset to a new ownere, the ovnership of asset changes to ower pub key.
	 * @param asset
	 * @param newOwner
	 * @throws Exception 
	 */
	public void transferAsset(Asset asset,String newOwner) throws Exception {
		
		ByteString newOwnerPubKey = null;
		//get the asset details from sawtooth
		
		// update asset with new owner pub key
		UpdateAsset value = UpdateAsset.newBuilder().setPubKey(newOwner).build();
		AssetPayload payload = AssetPayload.getDefaultInstance().newBuilder().setUdpateAsset(value).build();
		
		// error handling.
		assetPayloadClientService.submitStateChange(payload);
	}
}
