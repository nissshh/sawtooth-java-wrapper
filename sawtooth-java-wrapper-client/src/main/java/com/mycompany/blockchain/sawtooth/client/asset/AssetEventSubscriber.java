/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.asset;

import java.util.concurrent.TimeoutException;

import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.mycompany.blockchain.sawtooth.client.ClientZMQTemplate;
import com.mycompany.blockchain.sawtooth.client.EventListHandler;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IEventProcessor;
import com.mycompany.blockchain.sawtooth.core.service.IEventSubscriber;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import sawtooth.sdk.processor.exceptions.ValidatorConnectionError;
import sawtooth.sdk.protobuf.ClientEventsSubscribeRequest;
import sawtooth.sdk.protobuf.ClientEventsSubscribeResponse;
import sawtooth.sdk.protobuf.ClientEventsSubscribeResponse.Status;
import sawtooth.sdk.protobuf.EventFilter;
import sawtooth.sdk.protobuf.EventFilter.FilterType;
import sawtooth.sdk.protobuf.EventList;
import sawtooth.sdk.protobuf.EventSubscription;
import sawtooth.sdk.protobuf.Message;
import sawtooth.sdk.protobuf.Message.MessageType;

/**
 * A subscriber and listener for Asset TP
 * 
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
@Log
@AllArgsConstructor
public class AssetEventSubscriber implements IEventSubscriber<Asset> {

	private ClientZMQTemplate template;
	
	private IEventProcessor<Asset> eventProcessor;

	private IAddressBuilder<Asset> addressBuilder;
	
	public void init() throws Exception {
		if(template==null) {
			throw new Exception("Cannot start to subscribe as no ZMQ template specified");
		}
		template.init();
	}
	
	@Override
	public int subscribe() throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError, TimeoutException {
		EventFilter intkeyfilter = EventFilter.newBuilder().setKey("address").setMatchString(this.getAddressBuilder().getAddressPrefix())
				.setFilterType(FilterType.REGEX_ANY).build();

		EventSubscription eventSubscription = EventSubscription.newBuilder().setEventType("sawtooth/state-delta")
				.addFilters(intkeyfilter).build();

		ClientEventsSubscribeRequest clientEventsSubscribe = ClientEventsSubscribeRequest.newBuilder()
				.addSubscriptions(eventSubscription).build();
		ClientEventsSubscribeResponse response = template.subscribeClientEvent(clientEventsSubscribe);
		log.fine("Plain Response Message : "+response.getResponseMessage());
		log.info("Response Message : "+(JsonFormat.printToString(response)));
		if(Status.OK ==  response.getStatus()) {
			log.info("Response Message is OK and hence subscribed. Now starting to listener....." );
			while (true) {
				listen();
			}
		}
		return 1;
	}

	/**
	 * Start listening
	 * 
	 * @throws TimeoutException
	 * 
	 * 
	 */
	public void listen() throws TimeoutException {
		Message message = template.getStream().receive(5000L);
		log.info("Recieved Message :::::::::  " + message);
		try {
			if (message.getMessageType().equals(MessageType.CLIENT_EVENTS)) {
				// create a new thread to handle the event.
				EventList eventList = EventList.parseFrom(message.getContent());
				log.fine("State Changed to " + eventList);
				EventListHandler handler = new EventListHandler(eventList,getEventProcessor());
				Thread handlerTh = new Thread(handler);
				handlerTh.start();// handle event async
			} else {
				log.info("Other Events Recieved.." + message.getMessageType());
			}
		} catch (InvalidProtocolBufferException e) {
			log.warning("Cannot parse the incoming message to Event List: " + e.getMessage());
		}
	}
	
	@Override
	public IEventProcessor getEventProcessor() {
		return eventProcessor;
	}
	
	@Override
	public IAddressBuilder<Asset> getAddressBuilder() {
		return addressBuilder;
	}

}
