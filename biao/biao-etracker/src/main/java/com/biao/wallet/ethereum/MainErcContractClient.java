package com.biao.wallet.ethereum;

import com.biao.constant.Constant;
import com.biao.init.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 基于ERC20的代币
 */
public class MainErcContractClient {
    private static Logger logger = LoggerFactory.getLogger(MainErcContractClient.class);
    private static Web3j web3j;

    private static Admin admin;
    private static String emptyAddress = "0x0000000000000000000000000000000000000000";
    private static String fromAddress = "0x30188628a9f8345844adf08569863318bc0e6745";
    private static String toAddress = "0x68D0D42eFf127A6ee3f6b8783dd535f5b5CB0161";

    private static String contractAddress = "0x479bb409743d07cf47e2cf692bff62d174e72af1";

    public static void main(String[] args) throws IOException {
        web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        admin = Admin.build(new HttpService(Environment.RPC_URL));
        String rpcUrl = "http://47.88.175.183:5555";
    }

    /**
     * 查询代币余额
     */
    public static BigInteger getTokenBalance(Web3j web3j, String fromAddress, String contractAddress) {

        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

        EthCall ethCall;
        BigInteger balanceValue = BigInteger.ZERO;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            balanceValue = (BigInteger) results.get(0).getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return balanceValue;
    }

    /**
     * 查询代币名称
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static String getTokenName(Web3j web3j, String contractAddress) {
        String methodName = "name";
        String name = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            name = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 查询代币符号
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static String getTokenSymbol(Web3j web3j, String contractAddress) {
        String methodName = "symbol";
        String symbol = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            symbol = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return symbol;
    }

    /**
     * 查询代币精度
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static int getTokenDecimals(Web3j web3j, String contractAddress) {
        String methodName = "decimals";
        String fromAddr = emptyAddress;
        int decimal = 0;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            decimal = Integer.parseInt(results.get(0).getValue().toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return decimal;
    }

    /**
     * 查询代币发行总量
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static BigInteger getTokenTotalSupply(Web3j web3j, String contractAddress) {
        String methodName = "totalSupply";
        String fromAddr = emptyAddress;
        BigInteger totalSupply = BigInteger.ZERO;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            totalSupply = (BigInteger) results.get(0).getValue();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return totalSupply;
    }

    /**
     * 代币转账
     */
    public static String sendTokenTransaction(Admin admin, String fromAddress, String password, String toAddress, String contractAddress, BigInteger amount) {
        String txHash = null;
        try {
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(fromAddress, password, BigInteger.valueOf(300)).send();
            if (personalUnlockAccount.accountUnlocked()) {
                String methodName = "transfer";
                List<Type> inputParameters = new ArrayList<>();
                List<TypeReference<?>> outputParameters = new ArrayList<>();

                Address tAddress = new Address(toAddress);

                Uint256 value = new Uint256(amount);
                inputParameters.add(tAddress);
                inputParameters.add(value);

                TypeReference<Bool> typeReference = new TypeReference<Bool>() {
                };
                outputParameters.add(typeReference);

                Function function = new Function(methodName, inputParameters, outputParameters);

                String data = FunctionEncoder.encode(function);

                EthGetTransactionCount ethGetTransactionCount = admin
                        .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();
                BigInteger gasPrice = Constant.GAS_PRICE;//Convert.toWei(BigDecimal.valueOf(10), Convert.Unit.GWEI).toBigInteger();
                System.out.println(fromAddress + " 正在转出：" + "Nonce:" + nonce + "GasPrice:" + gasPrice);
                Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, gasPrice, Constant.GAS_LIMIT, contractAddress, data);
                EthSendTransaction ethSendTransaction = admin.ethSendTransaction(transaction).sendAsync().get();
                txHash = ethSendTransaction.getTransactionHash();
                System.out.println("转账结果:" + ethSendTransaction.getResult());
            } else {
                System.out.println("账户解锁失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return txHash;
    }
}