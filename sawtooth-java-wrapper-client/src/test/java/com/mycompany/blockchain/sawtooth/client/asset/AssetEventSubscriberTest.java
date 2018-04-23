/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.asset;

import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.client.ClientZMQTemplate;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IEventProcessor;
import com.mycompany.blockchain.sawtooth.core.service.asset.AssetAddressBuilder;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;

import sawtooth.sdk.processor.exceptions.ValidatorConnectionError;

/**
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
public class AssetEventSubscriberTest {

	private AssetEventSubscriber assetEventSubscriber;

	@Before
	public void init() throws Exception {
		ClientZMQTemplate template = new ClientZMQTemplate("tcp://localhost:4004");
		IEventProcessor<Asset> eventProcessor = new AssetEventProcessor();
		String txFamilyName = "asset";
		String txFamilyVer = "1.0";
		IAddressBuilder<Asset> addressBuilder = new AssetAddressBuilder(txFamilyName, txFamilyVer);
		assetEventSubscriber = new AssetEventSubscriber(template, eventProcessor, addressBuilder);
		assetEventSubscriber.init();
	}

	@Test
	public void testSubscribe()
			throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError, TimeoutException {
		assetEventSubscriber.subscribe();
	}

}
