/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.imple.asset;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

import com.mycompany.blockchain.sawtooth.core.service.wallet.WalletAddressBuilder;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.Wallet;


import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.CreateWallet;

/**
 * @author dev
 *
 */
public class WalletAddressBuilderTest {
	
	WalletAddressBuilder addressBuilder;

	private String address="710675acf86f5cf2b3cb9b2d22fb46c29ad5093f9c4d9500827ba5cfa0b8901e067c8a";
							//710675acf86f5cf2b3cb9b2d22fb46c29ad5093f9c4d9500827ba5cfa0b8901e067c8a
	
	private String customerId = "1259";
	
	private String txFamilyName="wallet";
	
	private String txFamilyVer="1.0";
	
	
	@Test
	public void test() throws UnsupportedEncodingException {
				
		Wallet wallet = Wallet.getDefaultInstance().newBuilder().setCustomerId(customerId).build();

		addressBuilder = new WalletAddressBuilder(txFamilyName, txFamilyVer);
		
		Assert.assertEquals(address,addressBuilder.buildAddress(wallet)); 
	}
}
