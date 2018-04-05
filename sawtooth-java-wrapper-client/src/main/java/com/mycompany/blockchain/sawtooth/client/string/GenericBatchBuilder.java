package com.mycompany.blockchain.sawtooth.client.string;

import java.util.List;

import com.google.protobuf.ByteString;

import sawtooth.sdk.client.Signing;
import sawtooth.sdk.protobuf.Batch;
import sawtooth.sdk.protobuf.BatchHeader;
import sawtooth.sdk.protobuf.BatchList;
import sawtooth.sdk.protobuf.Transaction;

class GenericBatchBuilder{
	/**
	 * The signer to signe batch and transactions
	 */
	Signer signer;
	
	public BatchList buildBatch(TransactionHeaderDTO transactionHeaderDTO){
		String publicKeyHex = signer.getSignerPrivateKey().getPublicKeyAsHex();
		
		
		BatchHeader batchHeader = BatchHeader.newBuilder()
				.clearSignerPublicKey()
				.setSignerPublicKey(publicKeyHex)
				.addTransactionIds(transactionHeaderDTO.getHeaderSignature())
				.build();

		ByteString batchHeaderBytes = batchHeader.toByteString();
		String batchHeaderSignature = Signing.sign(signer.getSignerPrivateKey(), batchHeader.toByteArray());
		Batch batch = Batch.newBuilder()
				.setHeader(batchHeaderBytes)
				.setHeaderSignature(batchHeaderSignature)
				.setTrace(true)
				.addTransactions(transactionHeaderDTO.getTransaction())
				.build();

		BatchList batchList = BatchList.newBuilder()
				.addBatches(batch)
				.build();
		return batchList;
	}

	public Signer getSigner() {
		return signer;
	}

	public void setSigner(Signer signer) {
		this.signer = signer;
	}
}