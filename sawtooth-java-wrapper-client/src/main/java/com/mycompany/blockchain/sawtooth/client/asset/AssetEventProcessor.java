/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.asset;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.core.service.IEventProcessor;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import sawtooth.sdk.processor.Utils;
import sawtooth.sdk.protobuf.Event;
import sawtooth.sdk.protobuf.StateChangeList;

/**
 * 
 * Process Event Data for asset 
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
@Log
public class AssetEventProcessor implements IEventProcessor<Asset> {

	@Override
	public void processEvent(Event e) throws InvalidProtocolBufferException {
		StateChangeList sc = StateChangeList.parseFrom(e.getData());
		log.info("Getting Value from Address - "+sc.getStateChangesList().get(0).getAddress());
		Asset asset = Asset.parseFrom(sc.getStateChangesList().get(0).getValue());
		log.info("Event Recieved for Asset Type : {}"+ asset);
	}

	@Override
	public void processEvent(String address, ByteString value) throws InvalidProtocolBufferException {
		Asset asset = Asset.parseFrom(value);
		log.info("Event Recieved for Asset Type :"+address + "Data :"+asset);
	}

}
