/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.imple.intkeyval;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.core.service.IParser;
import com.mycompany.blockchain.sawtooth.intkey.protobuf.IntKeyVal;

/**
 * 
 * Parses data to IntKeyVal
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class IntKeyValParser implements IParser<ByteString, IntKeyVal> {

	@Override
	public IntKeyVal parse(ByteString data) throws InvalidProtocolBufferException {
		return IntKeyVal.parseFrom(data);
	}

}
