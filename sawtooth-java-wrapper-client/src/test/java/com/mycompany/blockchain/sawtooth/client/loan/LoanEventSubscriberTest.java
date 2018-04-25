package com.mycompany.blockchain.sawtooth.client.loan;

import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.client.BaseClientTest;
import com.mycompany.blockchain.sawtooth.client.ClientService;
import com.mycompany.blockchain.sawtooth.client.ClientZMQTemplate;
import com.mycompany.blockchain.sawtooth.client.IEventProcessor;
import com.mycompany.blockchain.sawtooth.client.asset.AssetPayloadClientService;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.loan.LoanAddressBuilder;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;

import sawtooth.sdk.processor.exceptions.ValidatorConnectionError;

public class LoanEventSubscriberTest extends BaseClientTest{
	private LoanEventSubscriber eventSubscriber;
	@Before
	public void init() throws Exception {
		ClientZMQTemplate template = new ClientZMQTemplate("tcp://localhost:4004");
		AssetPayloadClientService assetPayloadClientService = (AssetPayloadClientService) getClientService();
		assetPayloadClientService.init();
		String txFamilyName = "loan";
		String txFamilyVer = "1.0";
		IAddressBuilder<Loan> addressBuilder = new LoanAddressBuilder(txFamilyName, txFamilyVer);
		IEventProcessor<Loan> eventProcessor = new LoanEventProcessor(assetPayloadClientService,addressBuilder);
		eventSubscriber = new LoanEventSubscriber(eventProcessor,addressBuilder,template);
		eventSubscriber.init();
	}

	@Test
	public void testSubscribe()
			throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError, TimeoutException {
		eventSubscriber.subscribe();
	}

	@Override
	protected ClientService getClientService() {
		 AssetPayloadClientService assetClientService = new AssetPayloadClientService("asset", "1.0", null, getZMQAddress());
		 return assetClientService;
	}
	
	@Override
	protected String getZMQAddress() {
		return "tcp://localhost:4004";
	}
}
