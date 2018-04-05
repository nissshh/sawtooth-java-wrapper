/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.imple.intkeyval;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IBaseDAO;
import com.mycompany.blockchain.sawtooth.core.service.IEntityConvertor;
import com.mycompany.blockchain.sawtooth.core.service.ITransactionHandler;
import com.mycompany.blockchain.sawtooth.core.service.IValidator;
import com.mycompany.blockchain.sawtooth.core.service.impl.string.StringToStringParser;
import com.mycompany.blockchain.sawtooth.core.service.impl.string.StringTranscationHandler;
import com.mycompany.blockchain.sawtooth.core.service.util.TPProcessRequestHelper;
import com.mycompany.blockchain.sawtooth.intkey.protobuf.IntKeyVal;

import sawtooth.sdk.processor.State;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.processor.exceptions.InvalidTransactionException;
import sawtooth.sdk.protobuf.TpProcessRequest;

/**
 * 
 * An Implementation to handle data of the types  String,IntKeyVal.
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class IntKeyValTransactionHandler implements ITransactionHandler<String, IntKeyVal> {
	
	private static Logger logger = Logger.getLogger(IntKeyValTransactionHandler.class.getName());

	private static final String TX_FAMILY_NAME="intkeyval";
	
	private static final String TX_FAMILY_VER="1.0";
	
	@Override
	public String transactionFamilyName() {
		return TX_FAMILY_NAME;
	}

	@Override
	public String getVersion() {
		return TX_FAMILY_VER;
	}

	private IntKeyValParser parser = new IntKeyValParser();

	private IEntityConvertor<ByteString,IntKeyVal,IntKeyValParser> entityConvertor;

	private IValidator<String> validator;
	
	public IntKeyValTransactionHandler() {
		entityConvertor = new IntKeyValConvertor(parser);
	}
	
	@Override
	public void apply(TpProcessRequest transactionRequest, State state)
			throws InvalidTransactionException, InternalError {
		ByteString payload = TPProcessRequestHelper.getPayloadAsByteString(transactionRequest);
		
		IntKeyVal entity = entityConvertor.convert(payload);  

		//we are not updating the value here.
		validator.validate(entity);
		
		String address = getAddressBuilder().buildAddress(entity); //the request contains data and action.
		
		IntKeyVal currentData = getBaseDAO().getLedgerEntry(state, address);
		
		logger.info(String.format("Current Data for address %s is %s",address,currentData));
		
		
		Collection<String> addressUpdated = this.getBaseDAO().putLedgerEntry(state, address, entity);
		
	}

	@Override
	public IAddressBuilder<IntKeyVal> getAddressBuilder() {
		return new IAddressBuilder<IntKeyVal>() {
			
			@Override
			public String getTransactionFamilyVersion() {
				return TX_FAMILY_VER;
			}
			
			@Override
			public String getTransactionFamilyName() {
				return TX_FAMILY_VER;
			}
			
			@Override
			public String getEntityKey(IntKeyVal entity) {
				return entity.getCustomerName();
			}
		};
	}

	@Override
	public IBaseDAO<String, IntKeyVal> getBaseDAO() {
		return new IBaseDAO<String, IntKeyVal>() {
			
			@Override
			public Collection<String> putLedgerEntry(State stateInfo, String address, IntKeyVal data)
					throws InternalError, InvalidTransactionException {
				Map.Entry<String, ByteString> entry = new AbstractMap.SimpleEntry<String, ByteString>(address,data.toByteString());
				Collection<Map.Entry<String, ByteString>> newLedgerEntry = Collections.singletonList(entry);
				return stateInfo.setState(newLedgerEntry);
			}
			
			@Override
			public IntKeyVal getLedgerEntry(State stateInfo, String address) throws InternalError, InvalidTransactionException {
				Map<String, ByteString> currentLedgerEntry = getLedgerEntryList(stateInfo,address);
				ByteString curentValue = currentLedgerEntry.get(address);
				try {
					return IntKeyVal.parseFrom(curentValue);
				} catch (InvalidProtocolBufferException e) {
					throw new InternalError(e.getMessage());
				}
			}

			private Map<String, ByteString> getLedgerEntryList(State stateInfo,String address) throws InternalError, InvalidTransactionException {
				return stateInfo.getState(Collections.singletonList(address));
			}
		};
	}

}
