/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.loan;

import java.io.IOException;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IParser;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanRequestPayload;

/**
 * LoanRequestPayload Parser
 *
 */
public class LoanPayloadParser implements IParser<ByteString, LoanRequestPayload>{

	@Override
	public LoanRequestPayload parse(ByteString data) throws IOException {
		return LoanRequestPayload.parseFrom(data);
	}

}
