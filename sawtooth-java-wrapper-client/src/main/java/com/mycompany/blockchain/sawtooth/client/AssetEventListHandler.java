/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client;

import java.util.List;

import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import sawtooth.sdk.protobuf.Event;

/**
 * 
 * An event list handler manger and dispatcher . the role of this class is to
 * accept event and dispatch to handler asynchronously
 * 
 * Event List is received as
 * 
 * Optional[[event_type: "sawtooth/state-delta" attributes { key: "address"
 * value:
 * "1cf1264aa624fa573079918f86c958f503cecb210ec2b258092079105096dbbdd61976" }
 * data:
 * "\nX\nF1cf1264aa624fa573079918f86c958f503cecb210ec2b258092079105096dbbdd61976\022\f\241gnishant\031\001,\030\001"
 * ] ]
 * 
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */

@Log
@AllArgsConstructor
public class AssetEventListHandler implements IEventListHandler<Asset> {

	private List<Event> eventList;

	private IEventProcessor<Asset> eventProcessor;
	
	
	@Override
	public IEventProcessor<Asset> getEventProcessor() {
		return eventProcessor;
	}

	@Override
	public List<Event> getEventList() {
		return eventList;
	}

}
