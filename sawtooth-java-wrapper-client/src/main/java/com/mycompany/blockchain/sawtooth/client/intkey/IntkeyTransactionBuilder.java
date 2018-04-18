/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.intkey;

import java.io.ByteArrayOutputStream;

import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;

/**
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class IntkeyTransactionBuilder extends GenericTransactionBuilder<String,String>{

	@Override
	protected byte[] getEncodedPayload(String payload) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String[] splits = payload.split(" ");
		if (splits[0].equalsIgnoreCase("inc") || splits[0].equalsIgnoreCase("dec")
				|| splits[0].equalsIgnoreCase("set")) {
		 	new CborEncoder(baos).encode(new CborBuilder()
			 		.addMap()
			 		.put("Verb", splits[0])
			 	    .put("Name", splits[1])
			 	    .put("Value",Integer.valueOf(splits[2]))
			 	    .end()
			 	    .build());

		} else {
		 	new CborEncoder(baos).encode(new CborBuilder()
			 		.addMap()
			 		.put("Verb", "set")
			 	    .put("Name", splits[0])
			 	    .put("Value",Integer.valueOf(splits[2]))
			 	    .end()
			 	    .build());
		}
		
	 	byte[] encodedBytes = baos.toByteArray();
		return encodedBytes;
	}

	@Override
	protected String getEntity(String payload) {
		return payload;
	}
}