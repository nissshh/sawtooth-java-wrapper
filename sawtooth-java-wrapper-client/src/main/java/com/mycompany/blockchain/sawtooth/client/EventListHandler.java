/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.core.service.IEventListHandler;
import com.mycompany.blockchain.sawtooth.core.service.IEventProcessor;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import sawtooth.sdk.protobuf.Event;
import sawtooth.sdk.protobuf.Event.Attribute;
import sawtooth.sdk.protobuf.EventList;
import sawtooth.sdk.protobuf.StateChange;
import sawtooth.sdk.protobuf.StateChangeList;

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
@AllArgsConstructor
@Log
public class EventListHandler implements IEventListHandler<Asset> {

	private EventList eventList;

	private IEventProcessor<Asset> eventProcessor;

	@Override
	public void run() {
		Optional<List<Event>> optionalProject = Optional.ofNullable(eventList.getEventsList());
		if (optionalProject.isPresent()) {
			if (optionalProject.get().isEmpty()) {
				log.warning("No Data available in events list notifications");
			} else {
				optionalProject.get().forEach(ev -> {
					if (ev.getEventType().equals("sawtooth/state-delta")) {
						log.info("<<<<< State Change Event  revieved as  >>>>>");
						log.info("_____________________________________________");
						List<StateChange> scList;
						try {
							scList = StateChangeList.parseFrom(ev.getData()).getStateChangesList();
							if (!scList.isEmpty()) {
								scList.stream().forEach(sc -> {
									try {
										eventProcessor.processEvent(sc.getAddress(), sc.getValue());
									} catch (InvalidProtocolBufferException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								});
							} else {

								log.info("No changes found");
							}
							log.info("_____________________________________________");
						} catch (InvalidProtocolBufferException e) {
							log.warning("The event for address " + ev.getAttributesList()
									+ " is not processed and shoudl be persisted for manual. Data Bytes below . "
									+ e.getMessage());
							try {
								ByteArrayOutputStream errorStream = new ByteArrayOutputStream(); // put a file stream
								ev.getData().writeTo(errorStream);
								log.warning(errorStream.toString());
							} catch (IOException e2) {
								log.severe(e2.getMessage());
							}
						}
					} else {
						log.info("/'Address/' is not found in attributes returened.");
					}

				});
			}
		} else {
			log.warning("No events list available.");
		}
	}

	@Override
	public IEventProcessor<Asset> getEventProcessor() {
		return eventProcessor;
	}

}
