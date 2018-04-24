/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.loan;

import java.util.List;

import com.mycompany.blockchain.sawtooth.client.IEventListHandler;
import com.mycompany.blockchain.sawtooth.client.IEventProcessor;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;

import lombok.AllArgsConstructor;
import sawtooth.sdk.protobuf.Event;

/**
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
@AllArgsConstructor
public class LoanEventListHandler implements IEventListHandler<Loan> {
	
	private List<Event> eventList;

	private IEventProcessor<Loan> eventProcessor;
	

	@Override
	public IEventProcessor<Loan> getEventProcessor() {
		return eventProcessor;
	}

	@Override
	public List<Event> getEventList() {
		return eventList;
	}

}
