/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * A siginng user signs a transaction wiht it pvt key
 * 
 * @author Nishant Sonar nishant_sonar@yahoo.com
 *
 */
@Getter
@Setter
@ToString(exclude = { "pvtKey" })
public class SigningUser {

	// demographic details

	String username;

	String password;
	
	String role;

	// signing details

	String pvtKey;

	String pubKey;

}
