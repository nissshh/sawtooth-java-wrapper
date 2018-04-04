package com.mycompany.blockchain.sawtooth.core.service.imple.intkeyval;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.core.service.IEntityConvertor;
import com.mycompany.blockchain.sawtooth.intkey.protobuf.IntKeyVal;

public class IntKeyValConvertor implements IEntityConvertor<ByteString, IntKeyVal, IntKeyValParser>{
	
	private IntKeyValParser	parser;
	
	public IntKeyValConvertor(IntKeyValParser parser) {
		this.parser = parser;
	}
	
	@Override
	public IntKeyVal convert(ByteString data) throws InvalidProtocolBufferException {
		return parser.parse(data);
	}

	@Override
	public IntKeyValParser getParser() {
		return parser;
	}

}
