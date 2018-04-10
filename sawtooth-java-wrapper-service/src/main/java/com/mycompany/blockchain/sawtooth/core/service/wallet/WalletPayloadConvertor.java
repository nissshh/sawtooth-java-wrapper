/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.wallet;

import java.io.IOException;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IEntityConvertor;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload;

/**
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class WalletPayloadConvertor implements IEntityConvertor<ByteString, SawtoothWalletPayload , WalletPayloadParser> {

	WalletPayloadParser parser;
	
	public WalletPayloadConvertor(WalletPayloadParser parser) {
		this.parser = parser;
	}

	@Override
	public SawtoothWalletPayload convert(ByteString data) throws IOException {
		return parser.parse(data);
	}

	@Override
	public WalletPayloadParser getParser() {
		return parser;
	}
	
	

}
