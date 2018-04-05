package com.mycompany.blockchain.sawtooth.core.service.imple.intkeyval;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

public class IntKeyAddressBuilderTest {
	IntKeyAddressBuilder addressBuilder;
	
	
	@Test
	public void test() throws UnsupportedEncodingException {
		addressBuilder = new IntKeyAddressBuilder("intkey", "1.0");
		String address = addressBuilder.buildAddress("nishant");
		String expected = "1cf1264aa624fa573079918f86c958f503cecb210ec2b258092079105096dbbdd61976";
		
		Assert.assertTrue(address.equals(expected));
		
		address = addressBuilder.buildAddress("sandip");
		expected = "1cf1264373b6d9263d0081f39f209146cc4fa9490057ce7f4cfa85e26a136eda38697a";
	}
}
