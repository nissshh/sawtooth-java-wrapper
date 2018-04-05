/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.impl.string;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IBaseDAO;

import sawtooth.sdk.processor.State;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.processor.exceptions.InvalidTransactionException;

/**
 * Implementation for storing and retrieving value.
 * @author dev
 *
 */
public class StringBaseDAO implements IBaseDAO<String, String> {

	@Override
	public String getLedgerEntry(State stateInfo,String address) throws InternalError, InvalidTransactionException {
		Map<String, ByteString> currentLedgerEntry = getLedgerEntryList(stateInfo,address);
		return currentLedgerEntry.get(address).toStringUtf8();
	}

	private Map<String, ByteString> getLedgerEntryList(State stateInfo,String address) throws InternalError, InvalidTransactionException {
		return stateInfo.getState(Collections.singletonList(address));
	}
	
	@Override
	public Collection<String> putLedgerEntry(State stateInfo,String address, String data) throws InternalError, InvalidTransactionException {
		Map.Entry<String, ByteString> entry = new AbstractMap.SimpleEntry<String, ByteString>(address,ByteString.copyFromUtf8(data.toString()));
		Collection<Map.Entry<String, ByteString>> newLedgerEntry = Collections.singletonList(entry);
		return stateInfo.setState(newLedgerEntry);
	}

}
