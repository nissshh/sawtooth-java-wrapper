/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service;

/**
 * Handles the list of events that are accepted.
 * 
 * @author Nishant Sonar
 *
 */
public interface IEventListHandler<ENTITY> extends Runnable {

	public IEventProcessor<ENTITY> getEventProcessor();
}
