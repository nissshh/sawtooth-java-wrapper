/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.protobuf.InvalidProtocolBufferException;

import sawtooth.sdk.protobuf.Event;
import sawtooth.sdk.protobuf.StateChange;
import sawtooth.sdk.protobuf.StateChangeList;

/**
 * Handles the list of events that are accepted.
 * 
 * @author Nishant Sonar
 *
 */
public interface IEventListHandler<ENTITY> extends Runnable {

	public IEventProcessor<ENTITY> getEventProcessor();
	
	public List<Event> getEventList();
	
	static final Logger log = Logger.getLogger(IEventListHandler.class.getName()); 
	
	@Override
	public default  void run() {
		Optional<List<Event>> optionalProject = Optional.ofNullable(getEventList());
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
										getEventProcessor().processEvent(sc.getAddress(), sc.getValue());
									} catch (InvalidProtocolBufferException e) {
										log.severe("Error when processing for" + sc + " Exception Message: "
												+ e.getMessage());
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
						log.finest("Current Implementation doestn handle other than sawtooth/state-delta");
					}

				});
			}
		} else {
			log.warning("No events list available.");
		}
	}
}
