/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.loan;

import java.util.List;
import java.util.concurrent.TimeoutException;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.client.AssetEventListHandler;
import com.mycompany.blockchain.sawtooth.client.ClientZMQTemplate;
import com.mycompany.blockchain.sawtooth.client.IEventListHandler;
import com.mycompany.blockchain.sawtooth.client.IEventProcessor;
import com.mycompany.blockchain.sawtooth.client.IEventSubscriber;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import sawtooth.sdk.processor.exceptions.ValidatorConnectionError;
import sawtooth.sdk.protobuf.Event;

/**
 * 
 * A Loan Request event subscriber to handle loan request state changes.
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
@Log
@RequiredArgsConstructor
public class LoanEventSubscriber implements IEventSubscriber<Loan> {
	
	
	private final IEventProcessor<Loan> eventProcessor;

	private final IAddressBuilder<Loan> addressBuilder;
	
	private final ClientZMQTemplate template;
	
	private boolean error;
	
	@Override
	public boolean isError() {
		return false;
	}

	@Override
	public ClientZMQTemplate getTemplate() {
		return template;
	}

	@Override
	public IAddressBuilder<Loan> getAddressBuilder() {
		return addressBuilder;
	}

	@Override
	public IEventProcessor<Loan> getEventProcessor() {
		return eventProcessor;
	}

	@Override
	public IEventListHandler<Loan> buildEventListHandler(List<Event> eventList, IEventProcessor<Loan> iEventProcessor) {
		return new LoanEventListHandler(eventList, eventProcessor);
	}
	

}
