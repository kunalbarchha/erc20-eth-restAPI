package com.ethereumapi.ethereumapi;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
public class Entity {

    private BigInteger publicKey;
    private BigInteger privateKey;
    private String fromWallet;
    private String toWallet;
    private String value;
    private BigInteger gasLimit;
    private BigInteger gasPrice;
    private BigInteger nonce;
    private String txHash;
}
@Getter
@Setter
class Wallet{
    private String publicKeyHex;
    private String privateKeyHex;
    private String walletAddress;
    private String walletName;
}