/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mycompany.blockchain.sawtooth.client.asset.AssetPayloadClientService;

import lombok.Data;

/**
 * All application major configurations to be stored here, other bean configurations can be included here
 * or in this class package.
 * 
 * @author Nishant Sonar
 *
 */

@Configuration
@Data
public class ApplicationConfig {
	
	@Value("${asset.tx.family.name}")
	private String txFamily;
	
	@Value("${asset.tx.family.version}")
	private String txVersion;
	
	//@Value("${asset.user.signerkey}")
	private String signerKey;
	
	@Value("${network.zmq.adress}")
	private String address;

	@Bean
	public AssetPayloadClientService getAssetService() throws Exception {
		AssetPayloadClientService assetPayloadClientService = new AssetPayloadClientService(txFamily, txVersion, signerKey, address);
		assetPayloadClientService.init();
		return assetPayloadClientService;
	}
}
