package com.ethereumapi.ethereumapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.NoOpProcessor;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Erc20Service {
    Web3j web3j=Web3j.build(new HttpService("https://kovan.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));

    public String getBalance(String walletAddress) throws Exception {

        BigInteger balance=null;
        log.info("wallet address in method >>> " +walletAddress);
        Credentials credentials=Credentials.create("0xa054970aaf0a2df3841ea028f5cd00041f06710719d94d2e9a36781e406d45af");
        String contractAddress="0x33fcaef31a637c96207e99b1a1189e1eb4640976";
        String abi="[{'constant':true,'inputs':[],'name':'name','outputs':[{'name':'','type':'string'}],'payable':false,'type':'function'},{'constant':false,'inputs':[{'name':'_spender','type':'address'},{'name':'_value','type':'uint256'}],'name':'approve','outputs':[{'name':'success','type':'bool'}],'payable':false,'type':'function'},{'constant':false,'inputs':[],'name':'releaseAdvisorTokens','outputs':[{'name':'success','type':'bool'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'totalSupply','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'constant':false,'inputs':[],'name':'allowTransfers','outputs':[],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'maxPresaleSupply','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'constant':false,'inputs':[{'name':'_from','type':'address'},{'name':'_to','type':'address'},{'name':'_value','type':'uint256'}],'name':'transferFrom','outputs':[{'name':'success','type':'bool'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'decimals','outputs':[{'name':'','type':'uint8'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'endTime','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'totalAllocated','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'crowdFundAddress','outputs':[{'name':'','type':'address'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'standard','outputs':[{'name':'','type':'string'}],'payable':false,'type':'function'},{'constant':false,'inputs':[{'name':'_token','type':'address'},{'name':'_to','type':'address'},{'name':'_amount','type':'uint256'}],'name':'withdrawTokens','outputs':[],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'minCrowdsaleAllocation','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'enjinTeamAllocation','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'constant':true,'inputs':[{'name':'','type':'address'}],'name':'balanceOf','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'constant':false,'inputs':[],'name':'retrieveUnsoldTokens','outputs':[{'name':'success','type':'bool'}],'payable':false,'type':'function'},{'constant':false,'inputs':[],'name':'acceptOwnership','outputs':[],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'owner','outputs':[{'name':'','type':'address'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'symbol','outputs':[{'name':'','type':'string'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'advisorAddress','outputs':[{'name':'','type':'address'}],'payable':false,'type':'function'},{'constant':false,'inputs':[{'name':'_amount','type':'uint256'}],'name':'addToAllocation','outputs':[],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'incentivisationAllocation','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'constant':false,'inputs':[{'name':'_to','type':'address'},{'name':'_value','type':'uint256'}],'name':'transfer','outputs':[{'name':'success','type':'bool'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'ENJ_UNIT','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'incentivisationFundAddress','outputs':[{'name':'','type':'address'}],'payable':false,'type':'function'},{'constant':false,'inputs':[],'name':'releaseEnjinTeamTokens','outputs':[{'name':'success','type':'bool'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'newOwner','outputs':[{'name':'','type':'address'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'totalAllocatedToAdvisors','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'enjinTeamAddress','outputs':[{'name':'','type':'address'}],'payable':false,'type':'function'},{'constant':true,'inputs':[{'name':'','type':'address'},{'name':'','type':'address'}],'name':'allowance','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'totalAllocatedToTeam','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'constant':false,'inputs':[{'name':'_newOwner','type':'address'}],'name':'transferOwnership','outputs':[],'payable':false,'type':'function'},{'constant':true,'inputs':[],'name':'advisorsAllocation','outputs':[{'name':'','type':'uint256'}],'payable':false,'type':'function'},{'inputs':[{'name':'_crowdFundAddress','type':'address'},{'name':'_advisorAddress','type':'address'},{'name':'_incentivisationFundAddress','type':'address'},{'name':'_enjinTeamAddress','type':'address'}],'payable':false,'type':'constructor'},{'anonymous':false,'inputs':[{'indexed':true,'name':'_from','type':'address'},{'indexed':true,'name':'_to','type':'address'},{'indexed':false,'name':'_value','type':'uint256'}],'name':'Transfer','type':'event'},{'anonymous':false,'inputs':[{'indexed':true,'name':'_owner','type':'address'},{'indexed':true,'name':'_spender','type':'address'},{'indexed':false,'name':'_value','type':'uint256'}],'name':'Approval','type':'event'},{'anonymous':false,'inputs':[{'indexed':false,'name':'_prevOwner','type':'address'},{'indexed':false,'name':'_newOwner','type':'address'}],'name':'OwnerUpdate','type':'event'}]'";
        NoOpProcessor processor = new NoOpProcessor(web3j);

        TransactionManager txManager = new FastRawTransactionManager(web3j, credentials, processor);
        log.info("Transaction manage >>>> " +txManager.toString());

        ERC20 token=ERC20.load(contractAddress,web3j,txManager, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
        log.info("Token ERC20 >>>>>> " + token.toString());

        balance=token.balanceOf(walletAddress).sendAsync().get();
        log.info("Balance >>>>>> " +balance.toString());

        return balance.toString();
    }
}
