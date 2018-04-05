/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service;

/**
 * A parser that will comvert from data to .
 * @author dev
 *
 * @param <FROM>
 * @param <TO>
 */
public interface IParser <FROM,TO>{

	TO parse(FROM data);
}
