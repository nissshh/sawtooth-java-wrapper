package com.mycompany.blockchain.sawtooth.client.wallet;

import com.mycompany.blockchain.sawtooth.client.GenericTransactionBuilder;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.Wallet;

public class WalletTransactionBuilder
		extends GenericTransactionBuilder<Wallet, SawtoothWalletPayload> {

	@Override
	protected byte[] getEncodedPayload(SawtoothWalletPayload payload) throws Exception {
		return payload.toByteArray(); // no encoding used.
	}

	@Override
	protected Wallet getEntity(SawtoothWalletPayload payload) {
		if (payload.hasCreateWallet()) {
			return Wallet.newBuilder().setCustomerId(payload.getCreateWallet().getCustomerId())
					.setBalance(payload.getCreateWallet().getInitialBalance()).build();
		} else if (payload.hasDeposit()) {
			return Wallet.newBuilder().setCustomerId(payload.getDeposit().getCustomerId())
					.setBalance(payload.getDeposit().getAmount()).build();
		} else if (payload.hasWithdraw()) {
			return Wallet.newBuilder().setCustomerId(payload.getWithdraw().getCustomerId())
					.setBalance(payload.getWithdraw().getAmount()).build();
		} else if (payload.hasTransferPayment()) {
			return Wallet.newBuilder()
					.setCustomerId(payload.getTransferPayment().getSourceCustomerId()).build();
		} else {
			return null;
		}
	}
}
