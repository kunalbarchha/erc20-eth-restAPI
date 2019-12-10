package com.ethereumapi.ethereumapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class WalletService {

    Entity entity = new Entity();
    Wallet wallet=new Wallet();
    public <List>Wallet createWallet(String walletName) throws Exception {

        ECKeyPair keyPair = Keys.createEcKeyPair();
        entity.setPublicKey(keyPair.getPublicKey());
        entity.setPrivateKey(keyPair.getPrivateKey());

        wallet.setPrivateKeyHex(Numeric.toHexStringWithPrefix(entity.getPrivateKey()));
        wallet.setPublicKeyHex(Numeric.toHexStringWithPrefix(entity.getPublicKey()));

        log.info("Private Key to Hex >>> " + wallet.getPrivateKeyHex());
        log.info("Public Key to Hex >>> " + wallet.getPublicKeyHex());

        Credentials credentials = Credentials.create(new ECKeyPair(entity.getPublicKey(), entity.getPrivateKey()));
        String walletAddress=credentials.getAddress();
        wallet.setWalletAddress(walletAddress);

        RepositoryService database = new RepositoryService();
        database.insert(walletName, wallet.getWalletAddress(), wallet.getPrivateKeyHex(), wallet.getPublicKeyHex());

        log.info("Wallet Address >>>>>>>" + walletAddress);
        return wallet;
    }

    public String getBalance(String walletAddress) throws Exception {

        String strTokenAmount = null;

        try {
            Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));
            EthGetBalance ethereum = web3j.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger wei = ethereum.getBalance();
            BigDecimal tokenValue = Convert.fromWei(String.valueOf(wei), Convert.Unit.ETHER);
            strTokenAmount = String.valueOf(tokenValue);

            log.info("Ether Balance : " + strTokenAmount);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return strTokenAmount;
    }
    public BigInteger requestCurrentGasPrice() throws Exception {

        Web3j web3j=Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));
        EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
        return ethGasPrice.getGasPrice();
    }

    private BigInteger getNonce(String from) throws ExecutionException, InterruptedException {

        Web3j web3j=Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
        log.info("Get Nounce DATA >>>>>>> " +transactionCount.toString());
        entity.setNonce(transactionCount.getTransactionCount());
        return entity.getNonce();
    }
    public String sendEther(String fromWallet, String privateKey, String toWallet, BigInteger value) throws Exception {

        String txHash=null;
        Web3j web3j=Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));

        BigInteger gasPrice = BigInteger.valueOf(20_000_000_000L);
        BigInteger gasLimit = BigInteger.valueOf(4_300_000);

        value = Convert.toWei("1.0", Convert.Unit.ETHER).toBigInteger();

        EthGetTransactionCount transactionCount=web3j.ethGetTransactionCount(fromWallet, DefaultBlockParameterName.LATEST).sendAsync().get();

        log.info("From wallet for Nonce >>>>>>>> " +fromWallet);
        BigInteger nonce=getNonce(fromWallet);

        log.info("Nonce >>>>>> " +nonce);
        Credentials credentials=Credentials.create(privateKey);

        RawTransaction rawTransaction=RawTransaction.createEtherTransaction(nonce, gasPrice,gasLimit,toWallet, value);

        byte[] signMessage=TransactionEncoder.signMessage(rawTransaction,credentials);
        String signedTransaction=Numeric.toHexString(signMessage);

        try{
            EthSendTransaction sendRawTransaction=web3j.ethSendRawTransaction(signedTransaction).sendAsync().get();
            txHash=sendRawTransaction.getTransactionHash();

            if (!txHash.isEmpty())
            {
                log.info("Transaction Hash >>>>>> " + txHash);
            }
            else
            {
                String error=sendRawTransaction.getError().getMessage();
                log.info("Error in send transaction >>>>>> " +error);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return txHash;
    }
}
