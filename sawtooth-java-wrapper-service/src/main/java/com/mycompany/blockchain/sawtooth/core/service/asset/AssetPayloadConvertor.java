/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.asset;

import java.io.IOException;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IEntityConvertor;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload;

/**
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class AssetPayloadConvertor implements IEntityConvertor<ByteString, AssetPayload, AssetPayloadParser> {

	AssetPayloadParser parser;
	
	public AssetPayloadConvertor(AssetPayloadParser parser) {
		this.parser = parser;
	}

	@Override
	public AssetPayload convert(ByteString data) throws IOException {
		return parser.parse(data);
	}

	@Override
	public AssetPayloadParser getParser() {
		return parser;
	}
	
	

}
