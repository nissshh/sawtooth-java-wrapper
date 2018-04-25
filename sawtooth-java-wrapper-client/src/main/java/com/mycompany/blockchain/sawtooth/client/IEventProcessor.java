/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;

/**
 * 
 * Implemented class will process events 
 * 
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
public interface IEventProcessor <ENTITY>  {

	/**
	 * Proces the event data as found
	 * @param address
	 * @param value
	 * @throws InvalidProtocolBufferException 
	 * @throws Exception 
	 */
	public void processEvent(String address, ByteString value) throws InvalidProtocolBufferException;

	
	/**
	 * An address builder that will help in filtering per addres.
	 * @return
	 */
	public IAddressBuilder<ENTITY> getAddressBuilder();

	}
