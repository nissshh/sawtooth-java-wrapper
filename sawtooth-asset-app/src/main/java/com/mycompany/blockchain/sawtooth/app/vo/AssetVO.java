/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.vo;

import lombok.Data;

/**
 * Value Object for the UI, UI can have its own schema mapped to the AssetVO that can be then converted to Asset
 * 
 * @author Nishant Sonar
 *
 */
@Data
public class AssetVO {

	String name;
	
	String details;
	
	int value;
	
	String owner;
	
}
