package com.ethereumapi.ethereumapi.Entity;

import lombok.Getter;
import lombok.Setter;

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
