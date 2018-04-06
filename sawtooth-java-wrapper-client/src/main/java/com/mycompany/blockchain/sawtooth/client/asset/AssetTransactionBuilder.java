package com.mycompany.blockchain.sawtooth.client.asset;

import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload;

public class AssetTransactionBuilder extends GenericTransactionBuilder<Asset, AssetPayload> {

	@Override
	protected byte[] getEncodedPayload(AssetPayload payload) throws Exception {
		return payload.toByteArray(); // no encoding used.
	}

	@Override
	protected Asset getEntity(AssetPayload payload) {
		if (payload.hasCreateAsset()) {
			return Asset.newBuilder().setName(payload.getCreateAsset().getName()).build();
		} else if (payload.hasUdpateAsset()) {
			return Asset.newBuilder().setName(payload.getUdpateAsset().getName()).build();
		} else {
			return null;
		}
	}
}
