/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.loan;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mycompany.blockchain.sawtooth.client.IEventProcessor;
import com.mycompany.blockchain.sawtooth.client.asset.AssetPayloadClientService;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Loan;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.UpdateAsset;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse.Status;

/**
 * 
 * @author Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
@RequiredArgsConstructor
@Log
public class LoanEventProcessor implements IEventProcessor<Loan> {

	private static final int CLOSURE_LIMIT = 0;
	
	private final AssetPayloadClientService service;
	
	private final IAddressBuilder<Loan> addressBuilder;
	
	@Override
	public void processEvent(String address, ByteString value) throws InvalidProtocolBufferException {
		if (address.subSequence(0, 6).equals(getAddressBuilder().getAddressPrefix())) { //This filter is not required, if event filter works. 
			Loan loan = Loan.parseFrom(value);
			log.info("Loan State  : " + loan);
			//if (loan.getBalance() <= CLOSURE_LIMIT) {
				String assetId = loan.getAssetId();
				String owner = loan.getBorrowerId();
				UpdateAsset updateAsset = UpdateAsset.newBuilder().setName(assetId).setPubKey(owner).build();
				AssetPayload payload = AssetPayload.newBuilder().setUdpateAsset(updateAsset).build();
				// raise a transaction and transfer asset
				try {
					log.info("Sending Asset Transfer on Loan Status {} Change Event " + loan);
					Status status = service.submitStateChange(payload);
					if(Status.OK!= status) {
						log.severe("Submission of Asset transfer geenrated error. Please check blockchain information ");
					}
				} catch (Exception e) {
					log.severe("An error occured when transfering the Asset " + assetId + " to Owner : " + owner
							+ " on Loan : " + loan.getId() + "  Status : " + loan.getStatus());
				}
			//}
		} else {
			log.fine("Skipped address " + address + "as it doesnt match " + getAddressBuilder().getAddressPrefix());
		}
	}

	@Override
	public IAddressBuilder<Loan> getAddressBuilder() {
		return addressBuilder;
	}

}
