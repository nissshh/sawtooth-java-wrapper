/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.payment;

import java.io.IOException;
import java.util.logging.Logger;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IBaseDAO;
import com.mycompany.blockchain.sawtooth.core.service.IDataValidator;
import com.mycompany.blockchain.sawtooth.core.service.IEntityConvertor;
import com.mycompany.blockchain.sawtooth.core.service.ITransactionHandler;
import com.mycompany.blockchain.sawtooth.loan.protobuf.Payment;
import com.mycompany.blockchain.sawtooth.loan.protobuf.PaymentPayload;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload;

import sawtooth.sdk.processor.State;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.processor.exceptions.InvalidTransactionException;
import sawtooth.sdk.protobuf.TpProcessRequest;

/**
 * 
 * @author Sandip Nirmal
 *
 */
public class PaymentHandler implements ITransactionHandler<String, SawtoothWalletPayload> {

	Logger log = Logger.getLogger(PaymentHandler.class.getName());

	public static final String TX_FAMILY_NAME = "payment";

	public static final String TX_FAMILY_VER = "1.0";

	// framwork usable classes
	private IAddressBuilder<Payment> loanAddressBuilder;

	private IEntityConvertor<ByteString, PaymentPayload, PaymentPayloadParser> entityConvertor;

	private IDataValidator<Payment> loanValidator;

	private PaymentPayloadParser loanPayloadParser;

	private IBaseDAO<String, Payment> loanDao;

	@Override
	public String transactionFamilyName() {
		return TX_FAMILY_NAME;
	}

	@Override
	public String getVersion() {
		return TX_FAMILY_VER;
	}

	public PaymentHandler() {
		loanAddressBuilder = new PaymentAddressBuilder(transactionFamilyName(), getVersion());
		loanPayloadParser = new PaymentPayloadParser();
		entityConvertor = new PaymentPayloadConvertor(loanPayloadParser);
		loanDao = new PaymentDAO();
		loanValidator = new PaymentValidator();
	}

	@Override
	public void apply(TpProcessRequest transactionRequest, State state)
			throws InvalidTransactionException, InternalError {
		log.info("Inside Apply got TP Request " + transactionRequest);
		try {
			PaymentPayload paymentPayload = entityConvertor
					.convert(transactionRequest.getPayload());
			switch (paymentPayload.getPaylodType()) {
			case CREATE_PAYMENT:
				createPayment(transactionRequest, state, paymentPayload);
				break;
			
			default:
				throw new InvalidTransactionException("Invalid Operation...");
			}
		} catch (IOException e) {
			throw new InternalError(e.getMessage());
		}
		log.info("Finished Apply got TP Request");
	}

	/**
	 * Method to Store Payment details in to the blockchain.
	 * @param transactionRequest
	 * @param state
	 * @param paymentPayload
	 * @throws InvalidTransactionException
	 * @throws InternalError
	 * @throws IOException
	 */
	 
	private void createPayment(TpProcessRequest transactionRequest, State state,
			PaymentPayload paymentPayload)
			throws InvalidTransactionException, InternalError, IOException {
		log.info("Inside createPayment()");
		Payment payment = paymentPayload.getPayment();		
		loanValidator.validate(paymentPayload.getPayment()); // validate data.

		String address = loanAddressBuilder.buildAddress(payment); // build address.
		
		loanDao.putLedgerEntry(state, address, payment); // persist data.
		log.info("Payment made with payment id " + payment.getId());
	}
	
	@Override
	public IAddressBuilder<SawtoothWalletPayload> getAddressBuilder() {
		return null;
	}

	@Override
	public IBaseDAO<String, SawtoothWalletPayload> getBaseDAO() {
		return null;
	}

}
