/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.impl.string;

import com.mycompany.blockchain.sawtooth.core.service.IEntityConvertor;

/**
 * an entity convertor from string to sting.
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class StringToStringEntityConvertor implements IEntityConvertor<String, String, StringToStringParser> {

	private StringToStringParser parser;

	public StringToStringEntityConvertor() {
		parser = new StringToStringParser();// todo move to config
	}

	public StringToStringEntityConvertor(StringToStringParser parser) {
		this.parser = parser;
	}

	@Override
	public String convert(String data) {
		return this.getParser().parse(data);
	}

	@Override
	public StringToStringParser getParser() {
		return parser;
	}

}
