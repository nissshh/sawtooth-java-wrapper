/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.vo;

import lombok.Data;

/**
 * Value Object for the UI, UI can have its own schema mapped to the LoanVO that can be then
 * converted to Loan
 * 
 * @author Sandip Nirmal
 *
 */
@Data
public class PaymentVO {

	int amount;
	String from;
	String to;	
}
