package com.ethereumapi.ethereumapi;

import com.ethereumapi.ethereumapi.Entity.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.GsonJsonParser;
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
import springfox.documentation.spring.web.json.Json;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class WalletService {

    Entity entity = new Entity();
    WalletEntity wallet=new WalletEntity();
    Web3j web3j = Web3j.build(new HttpService("https://kovan.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));

    @JsonSerialize
    public Map<String, String> createWallet(String walletName) throws Exception {

        Map<String,String> walletResponse=new HashMap<>();

        ECKeyPair keyPair = Keys.createEcKeyPair();
        entity.setPublicKey(keyPair.getPublicKey());
        entity.setPrivateKey(keyPair.getPrivateKey());

        wallet.setPrivateKeyHex(Numeric.toHexStringWithPrefix(entity.getPrivateKey()));
        walletResponse.put("PrivateKey",wallet.getPrivateKeyHex());
        wallet.setPublicKeyHex(Numeric.toHexStringWithPrefix(entity.getPublicKey()));
        walletResponse.put("PublicKey",wallet.getPublicKeyHex());

        log.info("Private Key to Hex >>> " + wallet.getPrivateKeyHex());
        log.info("Public Key to Hex >>> " + wallet.getPublicKeyHex());

        Credentials credentials = Credentials.create(new ECKeyPair(entity.getPublicKey(), entity.getPrivateKey()));
        String walletAddress=credentials.getAddress();
        wallet.setWalletAddress(walletAddress);
        walletResponse.put("WalletAddress",wallet.getWalletAddress());

        RepositoryService database = new RepositoryService();
        database.insert(walletName, wallet.getWalletAddress(), wallet.getPrivateKeyHex(), wallet.getPublicKeyHex());
        log.info("Wallet Address >>>>>>>" + walletAddress);
        return walletResponse;
    }

    public String getBalance(String walletAddress) throws Exception {

        String strTokenAmount = null;

        try {
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
    public BigDecimal requestCurrentGasPrice() throws Exception {

        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        BigInteger gasPrice = ethGasPrice.getGasPrice();
        BigDecimal gas=Convert.fromWei(String.valueOf(gasPrice), Convert.Unit.GWEI                    );
        log.info("Gas Price from method >>>> {}", gas.toString());
        return gas.multiply(BigDecimal.valueOf(2));
    }

    private BigInteger getNonce(String from) throws ExecutionException, InterruptedException {

        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
        log.info("Get Nounce DATA >>>>>>> " +transactionCount.toString());
        entity.setNonce(transactionCount.getTransactionCount());
        return entity.getNonce();
    }
    public String sendEther(String fromWallet, String privateKey, String toWallet, BigDecimal value) throws Exception {

        String txHash=null;

        BigDecimal gasPrice = requestCurrentGasPrice(); //BigInteger.valueOf(20_000_000_000L);
        log.info("Gas Price >>>>>>> "+ gasPrice);
        BigInteger gasLimit = BigInteger.valueOf(4_300_000);

        value = Convert.toWei(value,Convert.Unit.ETHER);
        log.info("Ether Value >>>> "+value);

        //EthGetTransactionCount transactionCount=web3j.ethGetTransactionCount(fromWallet, DefaultBlockParameterName.LATEST).sendAsync().get();

        log.info("From wallet for Nonce >>>>>>>> " +fromWallet);
        BigInteger nonce=getNonce(fromWallet);

        log.info("Nonce >>>>>> " +nonce);
        Credentials credentials=Credentials.create(privateKey);

        RawTransaction rawTransaction=RawTransaction.createEtherTransaction(nonce, gasPrice.add(BigDecimal.valueOf(1)).toBigInteger(),gasLimit,toWallet, value.toBigInteger());

        byte[] signMessage=TransactionEncoder.signMessage(rawTransaction,credentials);
        String signedTransaction=Numeric.toHexString(signMessage);
        String error=null;

        try{
            EthSendTransaction sendRawTransaction=web3j.ethSendRawTransaction(signedTransaction).sendAsync().get();
//            error=sendRawTransaction.getError().getMessage();
  //          log.info("Error in send transaction >>>>>> " +error);
            txHash=sendRawTransaction.getTransactionHash();

            if (txHash==null)
            {
                error=sendRawTransaction.getError().getMessage();
                log.info("Error in send transaction >>>>>> " +error);
                //RepositoryService saveTransaction=new RepositoryService();
                //saveTransaction.updateRecord(fromWallet, toWallet, value.toBigInteger(), txHash);
            }
            else
            {
                log.info("Transaction Hash >>>>>> " + txHash);
                return txHash;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return error;
    }
}
