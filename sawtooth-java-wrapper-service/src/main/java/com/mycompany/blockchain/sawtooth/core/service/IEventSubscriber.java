/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.google.protobuf.InvalidProtocolBufferException;

import sawtooth.sdk.processor.exceptions.ValidatorConnectionError;

/**
 * 
 * Subscribes to events fora  perticular entity
 * 
 * @author Nishant Sonar
 *
 */
public interface IEventSubscriber<ENTITY> {

	/**
	 * Listens to events
	 * @throws TimeoutException 
	 */
	public void listen() throws TimeoutException;
	
	
	/**
	 * Subscribes to the Sawtooth ZMQ and starts listeneing.
	 * @return
	 * @throws ValidatorConnectionError 
	 * @throws InterruptedException 
	 * @throws InvalidProtocolBufferException 
	 * @throws TimeoutException 
	 */
	public int subscribe() throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError, TimeoutException;
	
	
	
	/**
	 * Provides address builder
	 * @return
	 */
	public IAddressBuilder<ENTITY> getAddressBuilder();


	/**
	 * The event processor based on entity.
	 * @return
	 */
	public IEventProcessor getEventProcessor();
}
