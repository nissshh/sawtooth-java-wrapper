/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.impl.string;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IBaseDAO;
import com.mycompany.blockchain.sawtooth.core.service.IEntityConvertor;
import com.mycompany.blockchain.sawtooth.core.service.ITransactionHandler;
import com.mycompany.blockchain.sawtooth.core.service.IDataValidator;
import com.mycompany.blockchain.sawtooth.core.service.util.TPProcessRequestHelper;

import sawtooth.sdk.processor.State;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.processor.exceptions.InvalidTransactionException;
import sawtooth.sdk.protobuf.TpProcessRequest;

/**
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class StringTranscationHandler implements ITransactionHandler<String, String> {
	private static Logger logger = Logger.getLogger(StringTranscationHandler.class.getName());

	private static String TXN_FAMILY_NAME = "string"; // to be initialized by spring

	private static String TXN_FAMILY_VER = "1.0"; // to be initialized by spring

	private String txnFamilyName;

	private String txnFamilyVer;

	
	//framwork usable classes
	private IAddressBuilder<String> addressBuilder;

	private IBaseDAO<String, String> baseDAO;
	
	private IEntityConvertor<String,String,StringToStringParser> entityConvertor;

	private IDataValidator<String> validator;
	
	public StringTranscationHandler() {
		txnFamilyName = TXN_FAMILY_NAME;
		txnFamilyVer = TXN_FAMILY_VER;
		addressBuilder = new StringAddressBuilder(txnFamilyName,txnFamilyVer);
		baseDAO = new StringBaseDAO();
		validator = new StringValidator();
		entityConvertor = new StringToStringEntityConvertor();
	}

	public StringTranscationHandler(String txnFamillyName, String txnFamilyVersion) {
		super();
		this.txnFamilyName = txnFamillyName;
		this.txnFamilyVer = txnFamilyVersion;
		addressBuilder = new StringAddressBuilder(txnFamilyName,txnFamilyVer);
		baseDAO = new StringBaseDAO();
		validator = new StringValidator();
		entityConvertor = new StringToStringEntityConvertor();
	}

	@Override
	public String transactionFamilyName() {
		return TXN_FAMILY_NAME;
	}

	@Override
	public String getVersion() {
		return TXN_FAMILY_VER;
	}

	/**
	 * Implementer to write the code to use the state and request parameters accordingly for handling.Basic implementaion toinclude
	 * 
	 * 1. Get the requested payload
	 * 2. Decode the Payload
	 * 3. Convert/Transform Payload to Entity
	 * 4. Get the address for the decoded entity from state. 
	 * 5. Get the value from the address for decoded entity.
	 * 6. Update the value 
	 * 7. Calculate the address for the entity (the entity should have the mechanism for decising the key)
	 * 8. Store the state at the desired location.
	 */
	@Override
	public void apply(TpProcessRequest transactionRequest, State state)
			throws InvalidTransactionException, InternalError {
		//TPProcessRequestHelper.getUserSigner(transactionRequest);
		String payload = TPProcessRequestHelper.getPayload(transactionRequest);
		// No convertor required as its a simple string  but we can use idempotent one
		// can be used in case the payload we got is of different representation or further decoding is required.
		String entity = null;
		try {
			entity = entityConvertor.convert(payload);
			String address = addressBuilder.buildAddress(entity);
			String currentData;
			currentData = this.getBaseDAO().getLedgerEntry(state, address);
			
			logger.info(String.format("Current Data for address %s is %s",address,currentData));
			//we are not updating the value here.
			validator.validate(entity);
			Collection<String> addressUpdated = this.getBaseDAO().putLedgerEntry(state, address, entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public IAddressBuilder<String> getAddressBuilder() {
		return addressBuilder;
	}

	@Override
	public IBaseDAO<String, String> getBaseDAO() {
		return baseDAO;
	}
}
