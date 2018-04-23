/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.payment;

import java.io.IOException;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IEntityConvertor;
import com.mycompany.blockchain.sawtooth.loan.protobuf.PaymentPayload;

/**
 * @author Sandip Nirmal
 *
 */
public class PaymentPayloadConvertor
		implements IEntityConvertor<ByteString, PaymentPayload, PaymentPayloadParser> {

	PaymentPayloadParser parser;

	public PaymentPayloadConvertor(PaymentPayloadParser parser) {
		this.parser = parser;
	}

	@Override
	public PaymentPayload convert(ByteString data) throws IOException {
		return parser.parse(data);
	}

	@Override
	public PaymentPayloadParser getParser() {
		return parser;
	}

}
