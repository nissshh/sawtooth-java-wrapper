/**
 * 
 */
package com.mycompany.blockchain.sawtooth.core.service.wallet;

import java.io.IOException;
import java.util.logging.Logger;

import com.google.protobuf.ByteString;
import com.mycompany.blockchain.sawtooth.core.service.IAddressBuilder;
import com.mycompany.blockchain.sawtooth.core.service.IBaseDAO;
import com.mycompany.blockchain.sawtooth.core.service.IEntityConvertor;
import com.mycompany.blockchain.sawtooth.core.service.ITransactionHandler;
import com.mycompany.blockchain.sawtooth.core.service.IValidator;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.CreateWallet;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.Deposit;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.TransferPayment;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.SawtoothWalletPayload.Withdraw;
import com.mycompany.blockchain.sawtooth.wallet.protobuf.Wallet;

import sawtooth.sdk.processor.State;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.processor.exceptions.InvalidTransactionException;
import sawtooth.sdk.protobuf.TpProcessRequest;

/**
 * 
 * @author Nishant Sonar<nishant_sonar@yahoo.com>
 *
 */
public class WalletHandler implements ITransactionHandler<String, SawtoothWalletPayload> {

	Logger log = Logger.getLogger(WalletHandler.class.getName());

	public static final String TX_FAMILY_NAME = "wallet";

	public static final String TX_FAMILY_VER = "1.0";

	public static final String CREDIT_ACTION = "CREDIT";

	public static final String DEBIT_ACTION = "DEBIT";

	// framwork usable classes
	private IAddressBuilder<Wallet> walletAddressBuilder;

	private IEntityConvertor<ByteString, SawtoothWalletPayload, WalletPayloadParser> entityConvertor;

	private IValidator<Wallet> walletValidator;

	private WalletPayloadParser walletPayloadParser;

	private IBaseDAO<String, Wallet> walletDao;

	@Override
	public String transactionFamilyName() {
		return TX_FAMILY_NAME;
	}

	@Override
	public String getVersion() {
		return TX_FAMILY_VER;
	}

	public WalletHandler() {
		walletAddressBuilder = new WalletAddressBuilder(transactionFamilyName(), getVersion());
		walletPayloadParser = new WalletPayloadParser();
		entityConvertor = new WalletPayloadConvertor(walletPayloadParser);
		walletDao = new WalletDAO();
		walletValidator = new WalletValidator();
	}

	@Override
	public void apply(TpProcessRequest transactionRequest, State state)
			throws InvalidTransactionException, InternalError {
		log.info("Inside Apply got TP Request " + transactionRequest);
		try {
			SawtoothWalletPayload walletPayload = entityConvertor
					.convert(transactionRequest.getPayload());
			switch (walletPayload.getPayloadType()) {
			case CREATE_WALLET:
				createWallet(transactionRequest, state, walletPayload.getCreateWallet());
				break;
			case DEPOSIT:
				depositToWallet(transactionRequest, state, walletPayload.getDeposit());
				break;
			case WITHDRAW:
				withdrawFromWallet(transactionRequest, state, walletPayload.getWithdraw());
				break;
			case SEND_PAYMENT:
				transferPayment(transactionRequest, state, walletPayload.getTransferPayment());
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
	 * Handles Action - Create Wallet
	 * 
	 * @param transactionRequest
	 * @param state
	 * @param createWallet
	 * @throws InvalidTransactionException
	 * @throws IOException
	 * @throws InternalError
	 */
	private void createWallet(TpProcessRequest transactionRequest, State state,
			CreateWallet createWallet)
			throws InvalidTransactionException, InternalError, IOException {
		log.info("Inside createWallet()");
		Wallet wallet = getWallet(transactionRequest, createWallet); // build data.
		walletValidator.validate(wallet); // validate data.

		String address = walletAddressBuilder.buildAddress(wallet); // build address.
		Wallet existingAsset = walletDao.getLedgerEntry(state, address);

		if (existingAsset != null
				&& wallet.getCustomerId().equalsIgnoreCase(existingAsset.getCustomerId())) {
			throw new InvalidTransactionException(
					"Wallet for customer " + wallet.getCustomerId() + " already exists.");
		}

		walletDao.putLedgerEntry(state, address, wallet); // persist data.
		log.info("Wallet created for customer " + wallet.getCustomerId() + " with initial balance "
				+ wallet.getBalance());
	}

	/**
	 * Handles Action - Deposit to Wallet
	 * 
	 * @param transactionRequest
	 * @param state
	 * @param createWallet
	 * @throws InvalidTransactionException
	 * @throws IOException
	 * @throws InternalError
	 */
	private void depositToWallet(TpProcessRequest transactionRequest, State state, Deposit deposit)
			throws InvalidTransactionException, InternalError, IOException {
		log.info("Inside depositToWallet()");
		Wallet wallet = getWallet(transactionRequest, deposit); // build data.
		walletValidator.validate(wallet); // validate data.

		String address = walletAddressBuilder.buildAddress(wallet); // build address.
		Wallet existingWallet = walletDao.getLedgerEntry(state, address);

		if (existingWallet == null) {
			throw new InvalidTransactionException("No wallet found for customer "
					+ wallet.getCustomerId() + ". First create a wallet and then deposit.");
		}
		Wallet updatedWallet = updateWalletBalance(transactionRequest, wallet.getCustomerId(),
				existingWallet.getBalance(), deposit.getAmount(), CREDIT_ACTION);
		walletDao.putLedgerEntry(state, address, updatedWallet); // persist data.
		log.info("Wallet updated for customer " + wallet.getCustomerId() + " with balance "
				+ updatedWallet.getBalance());
	}

	/**
	 * Handles Action - Withdraw from Wallet
	 * 
	 * @param transactionRequest
	 * @param state
	 * @param createWallet
	 * @throws InvalidTransactionException
	 * @throws IOException
	 * @throws InternalError
	 */
	private void withdrawFromWallet(TpProcessRequest transactionRequest, State state,
			Withdraw withdraw) throws InvalidTransactionException, InternalError, IOException {
		log.info("Inside withdrawFromWallet()");
		Wallet wallet = getWallet(transactionRequest, withdraw); // build data.
		walletValidator.validate(wallet); // validate data.

		String address = walletAddressBuilder.buildAddress(wallet); // build address.
		Wallet existingWallet = walletDao.getLedgerEntry(state, address);

		if (existingWallet == null) {
			throw new InvalidTransactionException("No wallet found for customer "
					+ wallet.getCustomerId() + ". First create a wallet and then withdraw.");
		}
		Wallet updatedWallet = updateWalletBalance(transactionRequest, wallet.getCustomerId(),
				existingWallet.getBalance(), withdraw.getAmount(), DEBIT_ACTION);
		walletDao.putLedgerEntry(state, address, updatedWallet); // persist data.
		log.info("Wallet updated for customer " + wallet.getCustomerId() + " with balance "
				+ updatedWallet.getBalance());
	}

	/**
	 * Handles Action - Transfer Payment
	 * 
	 * @param transactionRequest
	 * @param state
	 * @param createWallet
	 * @throws InvalidTransactionException
	 * @throws IOException
	 * @throws InternalError
	 */
	private void transferPayment(TpProcessRequest transactionRequest, State state,
			TransferPayment transferPayment)
			throws InvalidTransactionException, InternalError, IOException {
		log.info("Inside transferPayment()");

		// Debit Source Account
		Wallet sourceWallet = Wallet.newBuilder()
				.setCustomerId(transferPayment.getSourceCustomerId())
				.setBalance(transferPayment.getAmount()).build();
		walletValidator.validate(sourceWallet); // validate source data.

		String sourceAddress = walletAddressBuilder.buildAddress(sourceWallet); // build address.
		Wallet existingSourceWallet = walletDao.getLedgerEntry(state, sourceAddress);

		if (existingSourceWallet == null) {
			throw new InvalidTransactionException(
					"No wallet found for customer " + sourceWallet.getCustomerId()
							+ ". First create a wallet for Source customer and then transfer.");
		}
		Wallet updatedSourceWallet = updateWalletBalance(transactionRequest, sourceWallet.getCustomerId(),
				existingSourceWallet.getBalance(), transferPayment.getAmount(), DEBIT_ACTION);

		// Credit Dest Customer Account
		Wallet destWallet = Wallet.newBuilder().setCustomerId(transferPayment.getDestCustomerId())
				.setBalance(transferPayment.getAmount()).build();
		walletValidator.validate(destWallet); // validate dest data.

		String destAddress = walletAddressBuilder.buildAddress(destWallet); // build address.
		// Credit Dest Customer Account
		Wallet existingDestWallet = walletDao.getLedgerEntry(state, destAddress);

		if (existingDestWallet == null) {
			throw new InvalidTransactionException(
					"No wallet found for customer " + sourceWallet.getCustomerId()
							+ ". First create a wallet for target customer and then transfer.");
		}
		Wallet updateDestdWallet = updateWalletBalance(transactionRequest, destWallet.getCustomerId(),
				existingDestWallet.getBalance(), transferPayment.getAmount(), CREDIT_ACTION);
		
		walletDao.putLedgerEntry(state, destAddress, updateDestdWallet); // persist data.		
		walletDao.putLedgerEntry(state, sourceAddress, updatedSourceWallet); // persist data.
		log.info("Wallet updated for customer " + sourceWallet.getCustomerId() + " with balance "
				+ updateDestdWallet.getBalance());
		log.info("Wallet updated for customer " + destWallet.getCustomerId() + "with balance "
				+ updateDestdWallet.getBalance());
	}

	/**
	 * create an asset from request type
	 * 
	 * @param transactionRequest
	 * @param createWallet
	 * @return
	 */
	private Wallet getWallet(TpProcessRequest transactionRequest, CreateWallet createWallet) {
		return Wallet.newBuilder().setCustomerId(createWallet.getCustomerId())
				.setBalance(createWallet.getInitialBalance()).build();
	}

	/**
	 * create an asset from request type
	 * 
	 * @param transactionRequest
	 * @param withdraw
	 * @return
	 */
	private Wallet getWallet(TpProcessRequest transactionRequest, Withdraw withdraw) {
		return Wallet.newBuilder().setCustomerId(withdraw.getCustomerId())
				.setBalance(withdraw.getAmount()).build();
	}

	/**
	 * create an asset from request type
	 * 
	 * @param transactionRequest
	 * @param deposit
	 * @return
	 */
	private Wallet getWallet(TpProcessRequest transactionRequest, Deposit deposit) {
		return Wallet.newBuilder().setCustomerId(deposit.getCustomerId())
				.setBalance(deposit.getAmount()).build();
	}

	/**
	 * create an asset from request type
	 * 
	 * @param transactionRequest
	 * @param deposit
	 * @return
	 * @throws InvalidTransactionException
	 */
	private Wallet updateWalletBalance(TpProcessRequest transactionRequest, String customerId,
			int oldBalance, int newAmount, String operation) throws InvalidTransactionException {
		int finalAmount = 0;
		switch (operation) {
		case CREDIT_ACTION:
			finalAmount = oldBalance + newAmount;
			break;
		case DEBIT_ACTION:
			if (oldBalance < newAmount) {
				throw new InvalidTransactionException(
						"Withdrawal amount is more than the current balance. Please check your current balance.");
			}
			finalAmount = oldBalance - newAmount;
			break;
		}
		return Wallet.newBuilder().setCustomerId(customerId).setBalance(finalAmount).build();
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
