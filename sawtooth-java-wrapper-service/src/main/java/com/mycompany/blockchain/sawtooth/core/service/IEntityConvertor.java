/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service;

import java.io.IOException;

/**
 A converter that represents the logic to convert from an entity (usually
 * byte/string) to another 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 * @param <CORE>    core data , can be byte[],ByteString,String
 * @param <ENTITY>  target entity also can be a payload.
 * @param <PARSER>  parser to parse core data, if a complex parsig like encoding and decoding is required.
 */
public interface IEntityConvertor<CORE, ENTITY, PARSER> {

	ENTITY convert(CORE data) throws IOException;

	PARSER getParser();
}
