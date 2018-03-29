/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service;

/**
 * 
 * 
 * @author dev
 *
 */



/**
 A converter that represents the logic to convert from an entity (usually
 * byte/string) to another 
 * @author dev
 *
 * @param <CORE>    core data
 * @param <ENTITY>  target entity
 * @param <PARSER>  parser to parse core data
 */
public interface IEntityConvertor<CORE, ENTITY, PARSER> {

	ENTITY convert(CORE data);

	PARSER getParser();
}
