package com.ethereumapi.ethereumapi;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
        private BigDecimal value;
    }

    @Data
    public static class CreateWallet{
        private String walletName;
    }

    @Data
    public static class BalanceOfToken{
        private String walletAddress;
    }

    @Autowired
    WalletService walletService;

    @Autowired
    Erc20Service erc20Service;

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

    @JsonSerialize
    @PostMapping(value = "/create/",consumes = "application/json",produces = "application/json")
    public ResponseEntity<?>wallet(@RequestBody CreateWallet createWallet){

        try
        {
            log.info("Wallet name >>>> {}", createWallet.walletName);
            return new ResponseEntity<>(walletService.createWallet(createWallet.walletName), HttpStatus.OK);
        }

        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(value="/getTokenBalance/", consumes = "application/json", produces = "application/json")
        public ResponseEntity<?>balance(@RequestBody BalanceOfToken balance)
    {

            try {
                log.info("Wallet address for token {}", balance.walletAddress);
                return new ResponseEntity<>(erc20Service.getBalance(balance.walletAddress),HttpStatus.OK);
            }
            catch (Exception ne){
                ne.printStackTrace();
                return new ResponseEntity<>(ne.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }
}

