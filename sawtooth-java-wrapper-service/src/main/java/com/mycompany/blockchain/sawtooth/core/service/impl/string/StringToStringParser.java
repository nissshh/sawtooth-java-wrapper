/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.impl.string;

import com.mycompany.blockchain.sawtooth.core.service.IParser;

/**
 * 
 * A string to string parser that converts from a string to string without
 * changing data.
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class StringToStringParser implements IParser<String, String> {

	@Override
	public String parse(String data) {
		return data;
	}

}
