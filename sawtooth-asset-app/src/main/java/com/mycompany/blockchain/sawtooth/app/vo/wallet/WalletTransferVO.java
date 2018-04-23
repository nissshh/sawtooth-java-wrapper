/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.vo.wallet;

import lombok.Data;

/**
 * Value Object for the UI, UI can have its own schema mapped to the WalletVO that can be then
 * converted to Wallet
 * 
 * @author Sandip Nirmal
 *
 */
@Data
public class WalletTransferVO {
	String customerId;
	int balance;
	String destCustomerId;
}
