/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service;

import java.io.IOException;

/**
 * A parser that will comvert from data to . eg. Can use encoding and deciding  from base data. 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 * @param <FROM> the representation of from datra , mostly of the form String,byte[] and ByteString.
 * @param <TO>
 */
public interface IParser <FROM,TO>{

	TO parse(FROM data) throws IOException;
}
