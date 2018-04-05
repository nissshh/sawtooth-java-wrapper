/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client;

import java.io.ByteArrayOutputStream;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;

/**
 * @author dev
 *
 */
public class IntkeyTransactionBuilder extends GenericTransactionBuilder<String>{

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
}
	

	
//	@Override
//	TransactionHeaderDTO buildTransaction(byte[] payload) throws Exception {
//		return super.buildTransaction(encodePayload());
//	}
	

	   /**
  * The encoding with payload shoudl match with the expected with transactin processor
  * @return
  * @throws CborException
  */
// private static byte[] encodePayload() throws CborException {
// 	ByteArrayOutputStream baos = new ByteArrayOutputStream();
// 	new CborEncoder(baos).encode(new CborBuilder()
// 		.addMap()
// 		.put("Verb", "set")
// 	    .put("Name", "sandip2")
// 	    .put("Value", 100)
// 	    .end()
// 	    .build());
// 	byte[] encodedBytes = baos.toByteArray();
//		return encodedBytes;
//	}


//	@Override
//	public byte[] encodePayload(String payload) {
//		payload.split(" ")
//		return null;
//	}
	
//}
