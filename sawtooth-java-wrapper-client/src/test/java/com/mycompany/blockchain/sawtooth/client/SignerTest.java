package com.mycompany.blockchain.sawtooth.client;

import static org.junit.Assert.assertNotNull;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.junit.Before;
import org.junit.Test;

import sawtooth.sdk.client.Signing;

public class SignerTest {

	private Signer signer;

	@Before
	public void init() throws Exception {
		ECKey pKey = Signing.generatePrivateKey(null);
		NetworkParameters params = new MainNetParams();
		signer = new Signer(pKey.getPrivateKeyAsWiF(params));
	}

	@Test
	public void testGetSignedPrivateKey() {
		ECKey key = signer.getSignerPrivateKey();
		assertNotNull(key);
	}

}
