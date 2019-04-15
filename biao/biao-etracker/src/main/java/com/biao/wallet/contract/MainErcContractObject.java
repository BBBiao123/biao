package com.biao.wallet.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import rx.Observable;
import rx.functions.Func1;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * ""littleredhat
 */
public class MainErcContractObject extends Contract {
    private static Logger logger = LoggerFactory.getLogger(MainErcContractObject.class);
    // https://remix.ethereum.org/ Compile - Details - WEB3DEPLOY - data
    private static final String BINARY = "0x6060604052341561000f57600080fd5b60d38061001d6000396000f3006060604052600436106049576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806360fe47b114604e5780636d4ce63c14606e575b600080fd5b3415605857600080fd5b606c60048080359060200190919050506094565b005b3415607857600080fd5b607e609e565b6040518082815260200191505060405180910390f35b8060008190555050565b600080549050905600a165627a7a72305820b287ea3878f6f6cc6e7e3885be10bd9172b2074fbdc32fbfc8f7edd3f683e8d90029";

    private Event transferEvent() {
        return new Event(
                "Transfer",
                Arrays.asList(
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Uint256>() {
                        }));
    }

    private Event approvalEvent() {
        return new Event(
                "Approval",
                Arrays.asList(
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Uint256>() {
                        }));
    }

    /**
     * HelloWorld合约
     *
     * @param contractAddress 合约地址
     * @param web3j           RPC请求
     * @param credentials     钱包凭证
     * @param gasPrice        GAS价格
     * @param gasLimit        GAS上限
     */
    private MainErcContractObject(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice,
                                  BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    /**
     * 部署合约
     *
     * @param web3j       RPC请求
     * @param credentials 钱包凭证
     * @param gasPrice    GAS价格
     * @param gasLimit    GAS上限
     * @return
     */
    public static RemoteCall<MainErcContractObject> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        // 构造函数参数 NULL
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList());
        return deployRemoteCall(MainErcContractObject.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    /**
     * 加载合约
     *
     * @param contractAddress 合约地址
     * @param web3j           RPC请求
     * @param credentials     钱包凭证
     * @param gasPrice        GAS价格
     * @param gasLimit        GAS上限
     * @return
     */
    public static MainErcContractObject load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MainErcContractObject(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    /**
     * get
     */
    public RemoteCall<Uint256> get() {
        Function function = new Function("get", Arrays.<Type>asList(), Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
        }));
        return executeRemoteCallSingleValueReturn(function);
    }

    /**
     * getBalance
     */
    public RemoteCall<Uint256> getBalance(String addres) {
        List<Type> parms = new ArrayList();
        Function function = new Function("getBalance", parms, Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
        }));
        return executeRemoteCallSingleValueReturn(function);
    }

    /**
     * set
     */
    public RemoteCall<TransactionReceipt> set(int x) {
        Function function = new Function("set", Arrays.<Type>asList(new Uint256(x)), Arrays.<TypeReference<?>>asList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        Function function = new Function("balanceOf", Arrays.<Type>asList(new Address(_owner)), Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
        }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }


    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = transferEvent();
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = transactionReceipt.getLogs().get(valueList.indexOf(eventValues));
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = transferEvent();
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                System.out.println("dfdf");
                return typedResponse;
            }
        });
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        final Event event = approvalEvent();
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = transactionReceipt.getLogs().get(valueList.indexOf(eventValues));
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    //Modified
    public Observable<Map<ApprovalEventResponse, Log>> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = approvalEvent();
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).toMap(new Func1<Log, ApprovalEventResponse>() {
            public ApprovalEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<String> name() {
        Function function = new Function("name",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        Function function = new Function(
                "approve",
                Arrays.<Type>asList(new Address(_spender),
                        new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        Function function = new Function("totalSupply",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        Function function = new Function(
                "transferFrom",
                Arrays.<Type>asList(new Address(_from),
                        new Address(_to),
                        new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> decimals() {
        Function function = new Function("decimals",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> version() {
        Function function = new Function("version",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> symbol() {
        Function function = new Function("symbol",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new Address(_to),
                        new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferW(String _to, BigInteger _value, BigInteger _wei) {
        Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new Address(_to),
                        new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, _wei);
    }

    public RemoteCall<TransactionReceipt> approveAndCall(String _spender, BigInteger _value, byte[] _extraData) {
        Function function = new Function(
                "approveAndCall",
                Arrays.<Type>asList(new Address(_spender),
                        new Uint256(_value),
                        new org.web3j.abi.datatypes.DynamicBytes(_extraData)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        Function function = new Function("allowance",
                Arrays.<Type>asList(new Address(_owner),
                        new Address(_spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<MainErcContractObject> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialAmount, String _tokenName, BigInteger _decimalUnits, String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_initialAmount),
                new Utf8String(_tokenName),
                new Uint8(_decimalUnits),
                new Utf8String(_tokenSymbol)));
        return deployRemoteCall(MainErcContractObject.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class TransferEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger _value;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String _owner;

        public String _spender;

        public BigInteger _value;
    }

    public static void main(String[] args) throws IOException {


    }


}
