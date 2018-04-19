/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mycompany.blockchain.sawtooth.client.asset.AssetPayloadClientService;
import com.mycompany.blockchain.sawtooth.client.loan.LoanPayloadClientService;
import com.mycompany.blockchain.sawtooth.client.payment.PaymentPayloadClientService;
import com.mycompany.blockchain.sawtooth.client.wallet.WalletPayloadClientService;

import lombok.Data;

/**
 * All application major configurations to be stored here, other bean configurations can be included
 * here or in this class package.
 * 
 * @author Nishant Sonar
 *
 */

@Configuration
@Data
public class ApplicationConfig {

	@Value("${asset.tx.family.name}")
	private String assetTxFamily;

	@Value("${loan.tx.family.name}")
	private String loanTxFamily;

	@Value("${wallet.tx.family.name}")
	private String walletTxFamily;

	@Value("${payment.tx.family.name}")
	private String paymentTxFamily;

	@Value("${asset.tx.family.version}")
	private String assetTxVersion;

	@Value("${loan.tx.family.version}")
	private String loanTxVersion;

	@Value("${wallet.tx.family.version}")
	private String walletTxVersion;

	@Value("${payment.tx.family.version}")
	private String paymentTxVersion;

	// @Value("${asset.user.signerkey}")
	private String signerKey;

	@Value("${network.zmq.adress}")
	private String address;

	@Bean
	public AssetPayloadClientService getAssetService() throws Exception {
		AssetPayloadClientService assetPayloadClientService = new AssetPayloadClientService(
				assetTxFamily, assetTxVersion, signerKey, address);
		assetPayloadClientService.init();
		return assetPayloadClientService;
	}

	@Bean
	public LoanPayloadClientService getLoanPayloadSerice() throws Exception {
		LoanPayloadClientService loanPayloadClientService = new LoanPayloadClientService(
				loanTxFamily, loanTxVersion, signerKey, address);
		loanPayloadClientService.init();
		return loanPayloadClientService;
	}

	@Bean
	public WalletPayloadClientService getWalletPayloadService() throws Exception {
		WalletPayloadClientService walletPayloadClientService = new WalletPayloadClientService(
				walletTxFamily, walletTxVersion, signerKey, address);
		walletPayloadClientService.init();
		return walletPayloadClientService;
	}

	@Bean
	public PaymentPayloadClientService getPaymentPayloadService() throws Exception {
		PaymentPayloadClientService payloadClientService = new PaymentPayloadClientService(
				paymentTxFamily, paymentTxVersion, signerKey, address);
		payloadClientService.init();
		return payloadClientService;
	}

}
