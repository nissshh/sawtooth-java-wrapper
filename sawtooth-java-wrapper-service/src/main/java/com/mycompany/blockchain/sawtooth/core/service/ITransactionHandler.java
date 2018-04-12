/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

import sawtooth.sdk.processor.TransactionHandler;
import sawtooth.sdk.processor.Utils;

/**
 * 
 * Provides default implementation for address handler
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 */
public interface ITransactionHandler<KEY, ENTITY> extends TransactionHandler {

	/**
	 * Provide the default namespace address prefix for a transaction family
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	default String getNamespaceAddressPrefix() throws UnsupportedEncodingException {
		return Utils.hash512(transactionFamilyName().getBytes("UTF-8")).substring(0, 6);
	}

	/**
	 * A default implementation for the getNamespaces.
	 */
	@Override
	default Collection<String> getNameSpaces() {
		ArrayList<String> namespaces = new ArrayList<>();
		try {
			namespaces.add(getNamespaceAddressPrefix());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return namespaces;
	}

	/**
	 * provide an entity specific addres builderITransactionHandler
	 * 
	 * @return
	 */
	public IAddressBuilder<ENTITY> getAddressBuilder();

	/**
	 * Get the base dao for the given entity type
	 * 
	 * @return
	 */
	public IBaseDAO<KEY, ENTITY> getBaseDAO();
}
