/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.payment;

import java.io.IOException;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IParser;
import com.mycompany.blockchain.sawtooth.loan.protobuf.PaymentPayload;

/**
 * LoanRequestPayload Parser
 *
 */
public class PaymentPayloadParser implements IParser<ByteString, PaymentPayload>{

	@Override
	public PaymentPayload parse(ByteString data) throws IOException {
		return PaymentPayload.parseFrom(data);
	}

}
