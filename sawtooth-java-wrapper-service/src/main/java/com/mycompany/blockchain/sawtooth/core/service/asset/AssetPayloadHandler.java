/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.asset;


import java.io.IOException;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IBaseDAO;
import com.mycompany.blockchain.sawtooth.core.service.IDataValidator;
import com.mycompany.blockchain.sawtooth.core.service.IEntityConvertor;
import com.mycompany.blockchain.sawtooth.core.service.ITransactionHandler;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload.PayloadType;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.CreateAsset;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.DeleteAsset;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.UpdateAsset;

import lombok.extern.java.Log;
import sawtooth.sdk.processor.State;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.processor.exceptions.InvalidTransactionException;
import sawtooth.sdk.protobuf.TpProcessRequest;

/**
 * An implementation to do Asset based CRUD operations.
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
@Log
public class AssetPayloadHandler implements ITransactionHandler<String, AssetPayload> {
	
	public static final String TX_FAMILY_NAME="asset";
	
	public static final String TX_FAMILY_VER="1.0";
	
	//framwork usable classes
	private IAddressBuilder<Asset> assetAddressBuilder;

	private IEntityConvertor<ByteString,AssetPayload,AssetPayloadParser> entityConvertor;

	private IDataValidator<Asset> assetValidator;
	
	private AssetPayloadParser assetPayloadParser;
	
	private IBaseDAO<String, Asset> assetDao;

	@Override
	public String transactionFamilyName() {
		return TX_FAMILY_NAME;
	}

	@Override
	public String getVersion() {
		return TX_FAMILY_VER;
	}

	
	public AssetPayloadHandler() {
		assetAddressBuilder = new AssetAddressBuilder(transactionFamilyName(),getVersion());
		assetPayloadParser = new AssetPayloadParser();
		entityConvertor = new AssetPayloadConvertor(assetPayloadParser);
		assetDao= new AssetDAO();
		assetValidator= new AssetDataValidator();
	}
	
	@Override
	public void apply(TpProcessRequest transactionRequest, State state)
			throws InvalidTransactionException, InternalError {
		log.info("Inside Apply got TP Request " + transactionRequest);
		try {
			AssetPayload assetPayload = entityConvertor.convert(transactionRequest.getPayload());
			if(assetPayload.getPayloadType().equals(PayloadType.CREATE_ASSET)) {
				handle(transactionRequest, state, assetPayload.getCreateAsset());
			} else if(assetPayload.getPayloadType().equals(PayloadType.UPDATE_ASSET)) {
				handle(transactionRequest, state, assetPayload.getUdpateAsset());
			} else if(assetPayload.getPayloadType().equals(PayloadType.DELETE_ASSET)){
				//set the status of the payload to inactive.
				handle(transactionRequest, state, assetPayload.getDeleteAsset());
			}  else {
				throw new InvalidTransactionException("Invalid payload type sent "+assetPayload.getPayloadType());
			}
		} catch (IOException e) {
			throw new InternalError(e.getMessage());
		}
		log.info("Finished Apply got TP Request");
	}

	

	/**
	 * Handles Action UPDATE_ASSET
	 * @param transactionRequest
	 * @param state
	 * @param udpateAsset
	 * @throws InvalidTransactionException 
	 * @throws IOException 
	 * @throws InternalError 
	 */
	private void handle(TpProcessRequest transactionRequest, State state, UpdateAsset udpateAsset) throws InvalidTransactionException, InternalError, IOException {
		log.info("Inside com.mycompany.blockchain.sawtooth.core.service.asset.AssetHandler.handle(TpProcessRequest, State, UpdateAsset)");
		Asset asset = getAsset(transactionRequest, udpateAsset); //build data.
		String address = assetAddressBuilder.buildAddress(asset); //build address.
		Asset existingAsset = assetDao.getLedgerEntry(state, address);
		log.info("Handling "+udpateAsset.getDescriptorForType().getName()+" for Data "+existingAsset+" at address " + address);
		if(existingAsset==null) {
			throw new InvalidTransactionException("No asset exists with the name as "+ asset.getName());
		}
		assetValidator.validate(asset); //validate data.
		assetDao.putLedgerEntry(state, address, asset); //persist data.
		log.info("End com.mycompany.blockchain.sawtooth.core.service.asset.AssetHandler.handle(TpProcessRequest, State, UpdateAsset)");
	}



	/**
	 * Handles Action CREATE_ASSET
	 * @param transactionRequest
	 * @param state
	 * @param createAsset
	 * @throws InvalidTransactionException
	 * @throws InternalError
	 * @throws IOException
	 */
	protected void handle(TpProcessRequest transactionRequest, State state, CreateAsset createAsset)
			throws InvalidTransactionException, InternalError, IOException {
		log.info("Inside com.mycompany.blockchain.sawtooth.core.service.asset.AssetHandler.handle(TpProcessRequest, State, CreateAsset)");
		Asset asset = getAsset(transactionRequest, createAsset); //build data.
		String address = assetAddressBuilder.buildAddress(asset); //build address.
		Asset existingAsset = assetDao.getLedgerEntry(state, address);
		log.info("Handling "+createAsset.getDescriptorForType().getName()+" for Data "+existingAsset+" at address " + address);
		if(existingAsset!=null && existingAsset.getName().equals(createAsset.getName())) {
			throw new InvalidTransactionException("Cannot create asset as asset already exists with the name as "+ asset.getName());
		}
		assetValidator.validate(asset); //validate data.
		assetDao.putLedgerEntry(state, address, asset); //persist data.
		log.info("End com.mycompany.blockchain.sawtooth.core.service.asset.AssetHandler.handle(TpProcessRequest, State, CreateAsset)");
	}

	/**
	 * create an asset from request type
	 * @param transactionRequest
	 * @param createAsset
	 * @return
	 */
	private  Asset getAsset(TpProcessRequest transactionRequest, CreateAsset createAsset) {
		String pubKey = transactionRequest.getSignature(); //public key of the creator, creator is owner.
		return Asset.newBuilder()
				.setName(createAsset.getName())
				.setDetails(createAsset.getDetails())
				.setPublicKey(pubKey)
				.setValue(createAsset.getValue())
				.setType(createAsset.getType())
				.setOwner(createAsset.getOwner()).build();
	}

	/**
	 * create an asset from request type
	 * @param transactionRequest
	 * @param udpateAsset
	 * @return
	 */
	private Asset getAsset(TpProcessRequest transactionRequest, UpdateAsset udpateAsset) {
		String pubKey = transactionRequest.getSignature(); //public key of the creator, creator is owner.
		return Asset.newBuilder()
				.setName(udpateAsset.getName())
				.setDetails(udpateAsset.getDetails())
				.setPublicKey(pubKey)
				.setValue(udpateAsset.getValue())
				.setType(udpateAsset.getType())
				.setOwner(udpateAsset.getOwner()).build();
	}

	private void handle(TpProcessRequest transactionRequest, State state, DeleteAsset deleteAsset) throws InvalidTransactionException {
		throw new InvalidTransactionException("This payload type is not supported"+deleteAsset.getDescriptor().getName());
	}
	
	@Override
	public IAddressBuilder<AssetPayload> getAddressBuilder() {
		return null;
	}

	@Override
	public IBaseDAO<String, AssetPayload> getBaseDAO() {
		return null;
	}


}
