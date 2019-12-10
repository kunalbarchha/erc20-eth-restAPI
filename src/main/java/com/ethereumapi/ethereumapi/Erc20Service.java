package com.ethereumapi.ethereumapi;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class Erc20Service {
    Web3j web3j=Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));

    public String getBalance(String walletAddress){
        String balance=null;

        String contractAddress="0xeb69c7a8d07b86fe22e6e53586d14d8281275e7e";
        String abi="";

        return balance;
    }
}
