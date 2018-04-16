/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.loan;

import java.io.IOException;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IEntityConvertor;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload;

/**
 * @author Sandip Nirmal
 *
 */
public class LoanPayloadConvertor
		implements IEntityConvertor<ByteString, LoanRequestPayload, LoanPayloadParser> {

	LoanPayloadParser parser;

	public LoanPayloadConvertor(LoanPayloadParser parser) {
		this.parser = parser;
	}

	@Override
	public LoanRequestPayload convert(ByteString data) throws IOException {
		return parser.parse(data);
	}

	@Override
	public LoanPayloadParser getParser() {
		return parser;
	}

}
