/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.imple.intkeyval;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;

import sawtooth.sdk.processor.Utils;

/**
 * @author dev
 *
 */
public class IntKeyAddressBuilder implements IAddressBuilder<String> {

	private static Logger logger = Logger.getLogger(IntKeyAddressBuilder.class.getName());
	
	private String txFamilyName;
	
	private String txFamilyVer;

	public IntKeyAddressBuilder(String txFamilyName, String txFamilyVer) {
		this.txFamilyName = txFamilyName;
		this.txFamilyVer = txFamilyVer;
	}

	@Override
	public String getTransactionFamilyName() {
		return txFamilyName;
	}

	@Override
	public String getTransactionFamilyVersion() {
		return txFamilyVer;
	}

	@Override
	public String getEntityKey(String entity) {
		String[] splits = entity.split(" ");
		String key;	
		if (splits[0].equalsIgnoreCase("inc") || splits[0].equalsIgnoreCase("dec")
				|| splits[0].equalsIgnoreCase("set")) {
			key = splits[1];
		} else {
			key = splits[0];
		}
		logger.info("Building address for Key as --"+ key);
		return key;
	}
	
	@Override
	public String buildAddress(String entity) throws UnsupportedEncodingException {
		String hashedName =	Utils.hash512(getEntityKey(entity).getBytes("UTF-8"));
		return Utils.hash512(this.txFamilyName.getBytes("UTF-8")).substring(0, 6)
				+ hashedName.substring(hashedName.length() - 64);
	}

}
