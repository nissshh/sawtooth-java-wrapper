/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.asset;

import java.util.List;

import com.mycompany.blockchain.sawtooth.client.AssetEventListHandler;
import com.mycompany.blockchain.sawtooth.client.ClientZMQTemplate;
import com.mycompany.blockchain.sawtooth.client.IEventListHandler;
import com.mycompany.blockchain.sawtooth.client.IEventProcessor;
import com.mycompany.blockchain.sawtooth.client.IEventSubscriber;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import sawtooth.sdk.protobuf.Event;
import sawtooth.sdk.protobuf.EventList;

/**
 * A subscriber and listener for Asset TP
 * 
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
@Log
@RequiredArgsConstructor
public class AssetEventSubscriber implements IEventSubscriber<Asset> {

	private final IEventProcessor<Asset> eventProcessor;

	private final IAddressBuilder<Asset> addressBuilder;
	
	private final ClientZMQTemplate template;
	
	private boolean error;
	
	@Override
	public IAddressBuilder<Asset> getAddressBuilder() {
		return addressBuilder;
	}

	@Override
	public IEventProcessor<Asset> getEventProcessor() {
		return eventProcessor;
	}

	@Override
	public ClientZMQTemplate getTemplate() {
		return template;
	}
	
	@Override
	public IEventListHandler<Asset> buildEventListHandler(List<Event> eventList,IEventProcessor<Asset> iEventProcessor) {
		return new AssetEventListHandler(eventList, eventProcessor);
	}

	@Override
	public boolean isError() {
		return error;
	}
}
