/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.vo.loan;

import com.mycompany.blockchain.sawtooth.app.vo.PaymentVO;

import lombok.Data;

/**
 * Value Object for the UI, UI can have its own schema mapped to the LoanVO that can be then
 * converted to Loan
 * 
 * @author Sandip Nirmal
 *
 */
@Data
public class LoanVO {
	String lenderId;
	String borrowerId;
	String assetId;
	float roi;
	int approvedAmount;
	int requestedAmount;
	PaymentVO payment;
}