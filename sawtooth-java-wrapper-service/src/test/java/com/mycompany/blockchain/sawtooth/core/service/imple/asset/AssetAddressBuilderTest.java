/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.imple.asset;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

import com.mycompany.blockchain.sawtooth.core.service.asset.AssetAddressBuilder;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;

public class AssetAddressBuilderTest {

	private AssetAddressBuilder addressBuilder; 
	
	
	@Test
	public void test() throws UnsupportedEncodingException {
		addressBuilder = new AssetAddressBuilder("asset", "1.0");
		Asset asset = Asset.newBuilder().setName("Laptop").build();
		String address = addressBuilder.buildAddress(asset);
		String expected = "92a8fd3007c507540201670ced64b7bc02b44164c3d947a3910c631dc8fae24a06660b";
						 //92a8fd3007c507540201670ced64b7bc02b44164c3d947a3910c631dc8fae24a06660b
						 //92a8fd3007c507540201670ced64b7bc02b44164c3d947a3910c631dc8fae24a06660b
		Assert.assertEquals("Address are not equal", expected, address);
	}
}
