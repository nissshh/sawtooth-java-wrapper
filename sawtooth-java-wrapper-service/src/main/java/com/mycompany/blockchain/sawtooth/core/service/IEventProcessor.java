/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;

import sawtooth.sdk.protobuf.Event;

/**
 * 
 * Implemented class will process events 
 * 
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
public interface IEventProcessor <ENTITY>  {

	/**
	 * Process event
	 * @deprecated
	 * @param e
	 * @throws InvalidProtocolBufferException 
	 */
	public void processEvent(Event e) throws InvalidProtocolBufferException;

	
	/**
	 * Proces the event data as found
	 * @param address
	 * @param value
	 * @throws InvalidProtocolBufferException 
	 */
	public void processEvent(String address, ByteString value) throws InvalidProtocolBufferException;

	}
