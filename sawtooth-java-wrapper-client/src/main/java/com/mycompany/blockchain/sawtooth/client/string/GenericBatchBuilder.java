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
	
	public BatchList buildBatch(List<Transaction> transactions){
		String publicKeyHex = signer.getSignerPrivateKey().getPublicKeyAsHex();
		
		BatchHeader batchHeader = BatchHeader.newBuilder()
				.clearSignerPublicKey()
				.setSignerPublicKey(publicKeyHex)
				//.addTransactionIds(txn.getHeaderSignature())
				.build();

		ByteString batchHeaderBytes = batchHeader.toByteString();
		String batchHeaderSignature = Signing.sign(signer.getSignerPrivateKey(), batchHeader.toByteArray());
		Batch batch = Batch.newBuilder()
				.setHeader(batchHeaderBytes)
				.setHeaderSignature(batchHeaderSignature)
				.setTrace(true)
				.addAllTransactions(transactions)
				.build();

		BatchList batchList = BatchList.newBuilder()
				.addBatches(batch)
				.build();
		return batchList;
	}
}