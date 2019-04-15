package com.biao.wallet.ethereum;

import com.biao.wallet.contract.MainErcContractObject;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * Event log相关
 * 监听合约event
 */
public class ContractEvent extends Contract {
    private static String contractAddress = "0x4f878c0852722b0976a955d68b376e4cd4ae99e5";
    private static Web3j web3j;

    protected ContractEvent(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ContractEvent(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractBinary, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static void main(String[] args) {
        String rpcUrl = "http://47.88.175.183:5555";
        web3j = Web3j.build(new HttpService(rpcUrl));
        /**
         * 监听ERC20 token 交易
         */
        EthFilter filter = new EthFilter(
                DefaultBlockParameterName.LATEST,
                DefaultBlockParameterName.LATEST,
                contractAddress);
        Event event = new Event("Transfer",
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }));
        String topicData = EventEncoder.encode(event);
        filter.addSingleTopic(topicData);
        System.out.println(topicData);

        web3j.ethLogObservable(filter).subscribe(log -> {
            System.out.println(log.getBlockNumber());
            System.out.println(log.getTransactionHash());
            EventValues eventValues = staticExtractEventParameters(event, log);
            MainErcContractObject.TransferEventResponse typedResponse = new MainErcContractObject.TransferEventResponse();
            typedResponse.log = log;
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            System.out.println("============================" + typedResponse._from);
            List<String> topics = log.getTopics();
            for (String topic : topics) {
                System.out.println(topic);
            }
        });
    }
}
