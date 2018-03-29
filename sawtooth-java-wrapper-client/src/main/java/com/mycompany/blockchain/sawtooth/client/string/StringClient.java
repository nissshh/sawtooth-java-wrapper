/**
 * 
 */
package com.mycompany.blockchain.sawtooth.client.string;

import java.util.List;
import java.util.logging.Logger;

import com.mashape.unirest.http.exceptions.UnirestException;

import sawtooth.sdk.protobuf.Transaction;

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

interface TransactionBuilder<ENTITY>{
 List<Transaction> buildTransactions(ENTITY entity);
}