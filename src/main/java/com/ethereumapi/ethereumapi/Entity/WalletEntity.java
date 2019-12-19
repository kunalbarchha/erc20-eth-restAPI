package com.ethereumapi.ethereumapi.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletEntity {
    private String publicKeyHex;
    private String privateKeyHex;
    private String walletAddress;
    private String walletName;
}