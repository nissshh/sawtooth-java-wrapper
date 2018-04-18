/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.vo;

import lombok.Data;

/**
 * Value Object for the UI, UI can have its own schema mapped to the WalletVO that can be then converted to Wallet
 * 
 * @author Sandip Nirmal
 *
 */
@Data
public class WalletVO {

	String customerId;
	
	int balance;
	
	String destCustomerId; 
}
