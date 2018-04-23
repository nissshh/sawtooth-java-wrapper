/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service;

import com.google.protobuf.ByteString;

/**
 * Privides an implementation for encoding and decoding data. Mostlu will be
 * used in Data Convertor/Payload convertor classes allong with parsing .
 * Usefull in case where a further encoding / decoding is used for some of the
 * Transaction Family,e.g. Int Keyu
 * 
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
public interface IDataEncoder<ENTITY> {
	
	/**
	 * Encodes an entity data to a byte string
	 * @param e
	 * @return
	 */
	public ByteString encode(ENTITY e);
	
	/**
	 * Decodes a ByteString to Entity.
	 * @param byteStrign
	 * @return
	 */
	public ENTITY decode(ByteString byteStrign);

	
	public default String decodeFromBase64() {
		return null;
	}
}
