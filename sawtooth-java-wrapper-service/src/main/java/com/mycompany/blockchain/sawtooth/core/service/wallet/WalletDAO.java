/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.wallet;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.core.service.IBaseDAO;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.Wallet;

import sawtooth.sdk.processor.State;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.processor.exceptions.InvalidTransactionException;

/**
 * Wallet Dao
 *
 */
public class WalletDAO implements IBaseDAO<String, Wallet> {

	/* (non-Javadoc)
	 * @see com.mycompany.blockchain.sawtooth.core.service.IBaseDAO#getLedgerEntry(sawtooth.sdk.processor.State, java.lang.Object)
	 */
	@Override
	public Wallet getLedgerEntry(State stateInfo, String address) throws InternalError, InvalidTransactionException, InvalidProtocolBufferException {
		Map<String, ByteString> currentLedgerEntry = getLedgerEntryList(stateInfo,address);
		return Wallet.parseFrom(currentLedgerEntry.get(address));
	}

	/* (non-Javadoc)
	 * @see com.mycompany.blockchain.sawtooth.core.service.IBaseDAO#putLedgerEntry(sawtooth.sdk.processor.State, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Collection<String> putLedgerEntry(State stateInfo, String address, Wallet data)
			throws InternalError, InvalidTransactionException {
		Map.Entry<String, ByteString> entry = new AbstractMap.SimpleEntry<String, ByteString>(address,data.toByteString()); //persist as byte string
		Collection<Map.Entry<String, ByteString>> newLedgerEntry = Collections.singletonList(entry);
		return stateInfo.setState(newLedgerEntry);
	}

	private Map<String, ByteString> getLedgerEntryList(State stateInfo,String address) throws InternalError, InvalidTransactionException {
		return stateInfo.getState(Collections.singletonList(address));
	}
	

}
