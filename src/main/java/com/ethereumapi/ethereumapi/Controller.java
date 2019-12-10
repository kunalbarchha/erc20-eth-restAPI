package com.ethereumapi.ethereumapi;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/ethereum-service/api")

public class Controller {

    @Data
    public static class Transfer {

        private String fromWallet;
        private String privateKey;
        private String toAddress;
        private BigInteger value;
    }

    public static class CreateWallet{
        private String walletName;
    }

    @Autowired
    WalletService walletService;
    CreateWallet createWallet;

    @RequestMapping(value = "/{walletAddress}", method = RequestMethod.POST)
    public ResponseEntity<?> getBalance(@PathVariable String walletAddress) {
        log.info("Wallet Address {} ", walletAddress);

        try{
            return new ResponseEntity<>(walletService.getBalance(walletAddress), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/transfer/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> transferSend(@RequestBody Transfer transfer) {

        try {
            log.info("withdraw request: {} {} {} {} ",transfer.fromWallet, transfer.privateKey, transfer.toAddress, transfer.value);
            walletService.sendEther(
                    transfer.fromWallet,
                    transfer.privateKey,
                    transfer.toAddress,
                    transfer.value);
            }
            catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @PostMapping(value="/create/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> newWallet(@RequestBody CreateWallet createWallet){
        try
        {
            log.info("Wallet Name {} ", createWallet.walletName);
            walletService.createWallet(createWallet.walletName);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}

