/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.asset;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.client.IEventProcessor;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;

import lombok.extern.java.Log;

/**
 * 
 * Process Event Data for asset 
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
@Log
public class AssetEventProcessor implements IEventProcessor<Asset> {

	@Override
	public void processEvent(String address, ByteString value) throws InvalidProtocolBufferException {
		Asset asset = Asset.parseFrom(value);
		log.info("Event Recieved for Asset Type :"+address + "Data :"+asset);
	}

}
