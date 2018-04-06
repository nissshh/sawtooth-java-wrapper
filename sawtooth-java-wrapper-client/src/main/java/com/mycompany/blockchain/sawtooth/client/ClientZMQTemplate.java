/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mashape.unirest.http.exceptions.UnirestException;

import sawtooth.sdk.messaging.Future;
import sawtooth.sdk.messaging.Stream;
import sawtooth.sdk.processor.exceptions.ValidatorConnectionError;
import sawtooth.sdk.protobuf.ClientBatchSubmitRequest;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse.Status;
import sawtooth.sdk.protobuf.ClientStateGetRequest;
import sawtooth.sdk.protobuf.ClientStateGetResponse;
import sawtooth.sdk.protobuf.Message;

/**
 * A helpr class abastractg the ZMQ communications
 * 
 * Implementation is based on Sawtooth SDK Stream.
 * 
 * @author Nishant Sonar
 *
 */
public class ClientZMQTemplate {

	private String zmqAddress = "tcp://localhost:4004";

	private Stream stream;
	
	/**
	 * 
	 * @param address  ZMQ Addresss
	 */
	public ClientZMQTemplate(String address) {
		this.zmqAddress = address;
	}

	public void init() throws Exception {
		if (zmqAddress == null || zmqAddress.isEmpty()) {
			throw new Exception("No address specified for ZMQ!! ");
		}
		stream = new Stream(zmqAddress);
	}

	/**
	 * USes ZMQ based request response and Sawtooth SDK
	 * 
	 * @param address
	 *            address for the data or enity type.
	 * @return ByteString representation of the response object, while client knows
	 *         for what entity the address is requested for
	 * @throws UnirestException
	 * @throws ValidatorConnectionError
	 * @throws InterruptedException
	 * @throws InvalidProtocolBufferException
	 */
	public ByteString getClientGetStateRequest(String address)
			throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError {
		ClientStateGetRequest getRequest = ClientStateGetRequest.getDefaultInstance().newBuilder().setAddress(address)
				.build();
		Future responseFuture = this.stream.send(Message.MessageType.CLIENT_STATE_GET_REQUEST,
				getRequest.toByteString());
		ClientStateGetResponse getResponse = ClientStateGetResponse.getDefaultInstance()
				.parseFrom(responseFuture.getResult());
		return getResponse.getValue();
	}

	/**
	 * Submits a batch to the sawtooth network and returns the response status 
	 * @param batchList byte representation of all batch list to be sent to client
	 * @return response status for validation
	 * @throws InvalidProtocolBufferException
	 * @throws InterruptedException
	 * @throws ValidatorConnectionError
	 */
	public Status submitBatch(ByteString batchList) throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError {
		ClientBatchSubmitRequest request = ClientBatchSubmitRequest.getDefaultInstance().parseFrom(batchList);
		Future responseFuture = this.stream.send(Message.MessageType.CLIENT_BATCH_SUBMIT_REQUEST,request.toByteString());
		ClientBatchSubmitResponse response = ClientBatchSubmitResponse.parseFrom(responseFuture.getResult());
		return response.getStatus();
	}
	
}
