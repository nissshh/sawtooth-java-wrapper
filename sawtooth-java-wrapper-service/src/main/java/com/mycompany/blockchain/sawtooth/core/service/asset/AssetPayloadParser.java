/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.asset;

import java.io.IOException;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IParser;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload;

/**
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class AssetPayloadParser implements IParser<ByteString, AssetPayload>{

	@Override
	public AssetPayload parse(ByteString data) throws IOException {
		return AssetPayload.parseFrom(data);
	}

}
