package com.mycompany.blockchain.sawtooth.client.string;

import com.google.protobuf.ByteString;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ClientEndpointService {

	private String endpoint = "http://10.34.14.1:8008/batches";
	
	private String contentType = "application/octet-stream";
	
	public String submit(ByteString batchBytes) throws UnirestException {
		String serverResponse = Unirest
				.post(endpoint)
				.header("Content-Type", contentType)
				.body(batchBytes.toByteArray())
				.asString()
				.getBody();
		return serverResponse;
	}
}