/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.wallet;

import java.io.IOException;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IParser;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload;

/**
 * Wallet Parser
 *
 */
public class WalletPayloadParser implements IParser<ByteString, SawtoothWalletPayload>{

	@Override
	public SawtoothWalletPayload parse(ByteString data) throws IOException {
		return SawtoothWalletPayload.parseFrom(data);
	}

}
