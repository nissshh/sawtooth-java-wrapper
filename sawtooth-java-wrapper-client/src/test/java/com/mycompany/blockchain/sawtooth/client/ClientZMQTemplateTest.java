package com.mycompany.blockchain.sawtooth.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import lombok.extern.java.Log;
import sawtooth.sdk.processor.exceptions.ValidatorConnectionError;
import sawtooth.sdk.protobuf.Setting;

@Log
public class ClientZMQTemplateTest {

	private ClientZMQTemplate template;

	@Before
	public void init() throws Exception {
		template = new ClientZMQTemplate("tcp://localhost:4004");
		template.init();
	}

	@Test
	public void testGet() throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError {
		String setting0 = "000000a87cb5eafdcca6a8cde0fb0dec1400c5ab274474a6aa82c12840f169a04216b7"; // leaf address for settings autorized keys
		ByteString respone = template.getClientGetStateRequest(setting0);
		Setting setting = Setting.parseFrom(respone);
		Assert.assertTrue(setting.getEntriesCount() > 0); // at least one entry for default state for
																				// settings.
		System.out.println(setting.getEntries(0).getKey());
		System.out.println(setting.getEntries(0).getValue());
	}
	
}
