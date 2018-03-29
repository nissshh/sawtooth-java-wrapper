/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.string;

import java.util.logging.Logger;

import org.bitcoinj.core.ECKey;

import sawtooth.sdk.client.Signing;
import org.bitcoinj.core.ECKey;

import com.google.protobuf.ByteString;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import sawtooth.sdk.client.Signing;
import sawtooth.sdk.processor.Utils;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.protobuf.Batch;
import sawtooth.sdk.protobuf.BatchHeader;
import sawtooth.sdk.protobuf.BatchList;
import sawtooth.sdk.protobuf.Transaction;
import sawtooth.sdk.protobuf.TransactionHeader;

/**
 * 
 * A sample clietn to use the service classes
 * 
 * @author dev
 *
 */
public class StringClient {
	private static Logger logger = Logger.getLogger(StringClient.class.getName());
	public static void main(String[] args) throws UnirestException {
		if(args.length>=2) {
			StringClientService service = new StringClientService(args[0], args[1], args[2]);
			service.init();
			
			service.service();
			
			service.destroy();
			
		}	 else {
			logger.severe("Usage as - uses 3 args.");
		}

	}
}
interface IClientService<PAYLOAD>{
	public void init();
	public void buildBatch(PAYLOAD payload);
	public void request(PAYLOAD payload);
	public void submit();
	public void destroy();
}

interface Signer{
	static Logger logger = Logger.getLogger(Signer.class.getName());
	default ECKey getSignerPrivateKey(String signerKey) {
		ECKey privateKey = null;
		if(signerKey!=null) {
			privateKey = Signing.readWif(signerKey);
			logger.info("Signer Key is set from supplied Private Key");
		} else {
			privateKey = Signing.generatePrivateKey(null); // new random privatekey
			logger.warning("Using Random Signer Key may not work at private network.");
		}
		return privateKey;
	}
}

interface ClientEndpointService{
	default String submit(ByteString batchBytes) throws UnirestException {
		String serverResponse = Unirest.post("http://localhost:8008/batches")
				.header("Content-Type", "application/octet-stream")
				.body(batchBytes.toByteArray())
				.asString()
				.getBody();
		return serverResponse;
	}
}
class StringClientService {

	private static Logger logger = Logger.getLogger(StringClientService.class.getName());
	private String txFamily;
	private String txVersion;
	private String signerKey;

	public StringClientService(String txFamily, String txVersion, String ignerKey) {
		this.txFamily = txFamily;
		this.txVersion = txVersion;
		this.signerKey = signerKey;
	}

	ClientEndpointService clientService;
	Signer signer;
	
	public void init() {
		
	}

	public void service() throws UnirestException {
		ByteString batchBytes = null;
		logger.info("Response for submission is : "+ clientService.submit(batchBytes));
	}

	public void destroy() {

	}
}