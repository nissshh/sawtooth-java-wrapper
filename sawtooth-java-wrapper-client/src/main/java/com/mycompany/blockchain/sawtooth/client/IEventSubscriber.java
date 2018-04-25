/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client;

import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;

import sawtooth.sdk.processor.exceptions.ValidatorConnectionError;
import sawtooth.sdk.protobuf.ClientEventsSubscribeRequest;
import sawtooth.sdk.protobuf.ClientEventsSubscribeResponse;
import sawtooth.sdk.protobuf.ClientEventsSubscribeResponse.Status;
import sawtooth.sdk.protobuf.Event;
import sawtooth.sdk.protobuf.EventFilter;
import sawtooth.sdk.protobuf.EventFilter.FilterType;
import sawtooth.sdk.protobuf.EventList;
import sawtooth.sdk.protobuf.EventSubscription;
import sawtooth.sdk.protobuf.Message;
import sawtooth.sdk.protobuf.Message.MessageType;

/**
 * 
 * Subscribes to events for ENTITY 
 * 
 * @author Nishant Sonar
 *
 */
public interface IEventSubscriber<ENTITY> {
	
	static Logger log = Logger.getLogger(IEventSubscriber.class.getName());

	/**
	 * Listens to events
	 * @throws TimeoutException 
	 */
	public default void listen() throws TimeoutException{
	
		Message message = getTemplate().getStream().receive(5000L);
		log.info("Recieved Message :::::::::  " + message);
		try {
			if (message.getMessageType().equals(MessageType.CLIENT_EVENTS)) {
				// create a new thread to handle the event.
				EventList eventList = EventList.parseFrom(message.getContent());
				log.fine("State Changed to " + eventList);
				IEventListHandler<ENTITY> handler = buildEventListHandler(eventList.getEventsList(),getEventProcessor());
				Thread handlerTh = new Thread(handler);
				handlerTh.start();// handle event async
			} else {
				log.info("Other Events Recieved.." + message.getMessageType());
			}
		} catch (InvalidProtocolBufferException e) {
			log.warning("Cannot parse the incoming message to Event List: " + e.getMessage());
		}
	}


	/**
	 * Subscribes to the Sawtooth ZMQ and starts listeneing.
	 * @return
	 * @throws ValidatorConnectionError 
	 * @throws InterruptedException 
	 * @throws InvalidProtocolBufferException 
	 * @throws TimeoutException 
	 */
	 public default int subscribe()
			throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError, TimeoutException {
		EventFilter intkeyfilter = EventFilter.newBuilder()
				.setKey("address")
				.setMatchString(this.getAddressBuilder().getAddressPrefix())
				.setFilterType(FilterType.SIMPLE_ANY)
				.build();
		log.info("Subscribing to the events of addresss = "+intkeyfilter);
		// added filter on state delta and on specific address
		EventSubscription eventSubscription = EventSubscription.newBuilder().clearFilters().setEventType("sawtooth/state-delta")
				//.addFilters(intkeyfilter) //This filter is required to work currently the issue is that the sawtooth doesnt outs events for loan , hence comenting.
				.build();	

		ClientEventsSubscribeRequest clientEventsSubscribe = ClientEventsSubscribeRequest.newBuilder()
				.addSubscriptions(eventSubscription).build();
		ClientEventsSubscribeResponse response = this.getTemplate().subscribeClientEvent(clientEventsSubscribe);
		log.fine("Plain Response Message : " + response.getResponseMessage());
		log.info("Response Message : " + JsonFormat.printToString(response));
		if (Status.OK == response.getStatus()) {
			log.info("Response Message is OK and hence subscribed. Now starting to listener.....");
			while (!isError()) {
				listen();
			}
		}
		return isError() ? -1 : 1;
	}

	public default void init() throws Exception {
		if (this.getTemplate() == null) {
			throw new Exception("Cannot start to subscribe as no ZMQ template specified");
		}
		this.getTemplate().init();
	}

	
	/**
	 * Error Out condition
	 * @return
	 */
	boolean isError();
	
	/**
	 * Underlying resource for listening.
	 * @return
	 */
	public ClientZMQTemplate getTemplate();
	
	
	/**
	 * Provides address builder
	 * @return
	 */
	public IAddressBuilder<ENTITY> getAddressBuilder();


	/**
	 * The event processor based on entity.
	 * @return
	 */
	public IEventProcessor<ENTITY> getEventProcessor();
	
	
	/**
	 * 
	 * @param eventList
	 * @param iEventProcessor
	 * @return
	 */
	public IEventListHandler<ENTITY> buildEventListHandler(List<Event> eventList, IEventProcessor<ENTITY> iEventProcessor);
	
}
