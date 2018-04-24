/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.loan;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.client.IEventProcessor;
import com.mycompany.blockchain.sawtooth.client.asset.AssetPayloadClientService;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;
import com.mycompany.blockchain.sawtooth.loan.protobuf.LoanStatus;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.UpdateAsset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
@RequiredArgsConstructor
@Slf4j
public class LoanEventProcessor implements IEventProcessor<Loan> {

	private static final int CLOSURE_LIMIT = 0;
	
	private final AssetPayloadClientService service;
	
	@Override
	public void processEvent(String address, ByteString value) throws InvalidProtocolBufferException {
		Loan loan = Loan.parseFrom(value);
		if (loan.getBalance() <= CLOSURE_LIMIT && loan.getStatus().equals(LoanStatus.CLOSED)) {
			String assetId = loan.getAssetId();
			String owner = loan.getBorrowerId();
			UpdateAsset updateAsset = UpdateAsset.newBuilder().setName(assetId).setPubKey(owner).build();
			AssetPayload payload = AssetPayload.newBuilder().setUdpateAsset(updateAsset).build();
			//raise a transaction and transfer asset
			try {
				log.info("Sending Asset Transfer on Loan Status {} Change Event ",loan);
				service.submitStateChange(payload);
			} catch (Exception e) {
				log.error("An error occured when transfering the Asset {} to Owner : {} on Loan : {}  Status : {}",assetId,owner,loan.getId() ,loan.getStatus());
			}
		}
	}

}
